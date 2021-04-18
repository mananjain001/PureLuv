package com.mananjain.Purelove.Common.LoginSignup

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.chaos.view.PinView
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.*
import com.mananjain.Purelove.Databases.Data
import com.mananjain.Purelove.R
import java.util.concurrent.TimeUnit


class VerifyOTP : AppCompatActivity() {


    lateinit var codeBySystem: String
    lateinit var name: String
    lateinit var username: String
    lateinit var email: String
    lateinit var password: String
    lateinit var gender: String
    lateinit var dob: String
    lateinit var pmatch: String
    lateinit var nmatch: String
    lateinit var phonenumber: String

    lateinit var builder: AlertDialog.Builder

    lateinit var dialog : AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_o_t_p)

        name = intent.getStringExtra("Name")
        username = intent.getStringExtra("Username")
        email = intent.getStringExtra("Email")
        password = intent.getStringExtra("Password")
        gender = intent.getStringExtra("Gender")
        dob = intent.getStringExtra("DOB")
        phonenumber = intent.getStringExtra("PhoneNumber")

        val mobilenumber: TextView = findViewById(R.id.otpmobile_number)
        mobilenumber.text = phonenumber

        builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setView(R.layout.progress_bar_layout)
        dialog= builder.create()

        sendVerificationCodeToUser(phonenumber)


    }

    fun callnextscreenfromotp(view: View) {
        val pinView: PinView = findViewById(R.id.pin_view)
        val code = pinView.text.toString()
        if (!code.isEmpty()) {
            verifycode(code)
        }
    }

    fun exitfromverification(view: View) {

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Are you sure?")
        builder.setMessage("Do you want to cancel the verification ").setCancelable(true)
        builder.setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
            startActivity(Intent(this, Signup::class.java))
            finishAffinity()
        }
        builder.setNegativeButton("No") { dialogInterface: DialogInterface, i: Int -> }
        builder.show()
    }


    private fun sendVerificationCodeToUser(phonenumber: String?) {
        getmatches()
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phonenumber.toString(),
            60, // Timeout duration
            TimeUnit.SECONDS, // Unit of timeout
            TaskExecutors.MAIN_THREAD, // Activity (for callback binding)
            callbacks
        )
    }

    var callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            val code: String? = credential.smsCode
            if (code != null) {
                val pinView: PinView = findViewById(R.id.pin_view)
                pinView.setText(code)
                verifycode(code)
            }
        }


        override fun onVerificationFailed(e: FirebaseException) {
            Toast.makeText(application, e.message, Toast.LENGTH_SHORT).show()
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            codeBySystem = verificationId
        }
    }

    private fun verifycode(code: String) {
        dialog.show()
        val credential = PhoneAuthProvider.getCredential(codeBySystem, code)
        signInWithPhoneAuthCredential(credential)
    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {

        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    storeNewUserData()
                    Toast.makeText(
                        this,
                        "Verification Completed!",
                        Toast.LENGTH_SHORT
                    ).show()
                    intent = Intent(this, UpdateProfile::class.java)
                    intent.putExtra("PhoneNumber", phonenumber)
                    startActivity(intent)
                    finishAffinity()
                } else {

                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        dialog.dismiss()
                        Toast.makeText(
                            this,
                            "Verification not Completed! Try Again",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
            }
    }

    private fun getmatches()
    {
        val checkUser = FirebaseDatabase.getInstance().getReference("Developer")
        checkUser.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                pmatch = snapshot.child("priority").value.toString()
                nmatch = snapshot.child("normal").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@VerifyOTP, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun storeNewUserData() {

        val rootNode: FirebaseDatabase = FirebaseDatabase.getInstance()
        val userreference: DatabaseReference = rootNode.getReference("Users").child("Accounts")

        val database = Data(
            name,
            username,
            email,
            password,
            gender,
            dob,
            phonenumber,
            pmatch,
            nmatch,
            "Not Matching",
            "Null",
            "Null",
            "Null"
        )
        userreference.child(phonenumber).setValue(database)
    }
}