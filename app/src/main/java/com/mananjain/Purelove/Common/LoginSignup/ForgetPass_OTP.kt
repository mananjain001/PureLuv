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
import com.mananjain.Purelove.R
import java.util.concurrent.TimeUnit


class ForgetPass_OTP : AppCompatActivity() {

    var phonenumber: String? = null
    lateinit var codeBySystem:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_pass__o_t_p)
        phonenumber = intent.getStringExtra("PhoneNumber")

        val mobilenumber: TextView = findViewById(R.id.mobile_number)
        mobilenumber.text = phonenumber
        sendVerificationCodeToUser(phonenumber)
    }
    fun callnextscreenfromotp(view: View) {
        val pinView: PinView = findViewById(R.id.pin_view)
        val code = pinView.text.toString()
        if (!code.isEmpty()) {
            verifycode(code)
        }
    }
    private fun sendVerificationCodeToUser(phonenumber1: String?) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phonenumber1.toString(),
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
        val credential = PhoneAuthProvider.getCredential(codeBySystem, code)
        signInWithPhoneAuthCredential(credential)
    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {

        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    updateData()
                    Toast.makeText(
                        this@ForgetPass_OTP,
                        "Verification Completed!",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {

                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(
                            this,
                            "Verification not Completed! Try Again",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
    }

    private fun updateData() {
        intent = Intent(this, setNewPassword::class.java)
        intent.putExtra("PhoneNumber", phonenumber)
        startActivity(intent)
        finish()
    }

    fun exitfromverification(view: View) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Are you sure?")
        builder.setMessage("Do you want to cancel the verification ").setCancelable(true)
        builder.setPositiveButton("Yes") { dialogInterface: DialogInterface, i: Int ->
            startActivity(Intent(this, Login::class.java))
            finishAffinity()
        }
        builder.setNegativeButton("No") { dialogInterface: DialogInterface, i: Int -> }
        builder.show()
    }
}