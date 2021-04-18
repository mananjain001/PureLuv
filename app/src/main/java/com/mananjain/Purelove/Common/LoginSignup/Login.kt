package com.mananjain.Purelove.Common.LoginSignup


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.*
import com.hbb20.CountryCodePicker
import com.mananjain.Purelove.Databases.sessionManager
import com.mananjain.Purelove.R
import com.mananjain.Purelove.User.UserDashboard

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_login)

    }


    fun userLogin(view: View) {

        if (validatefields()) {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setCancelable(false)
            builder.setView(R.layout.progress_bar_layout)
            var dialog : AlertDialog = builder.create()
            dialog.show()
            val phone = findViewById<TextInputLayout>(R.id.login_phone_number)
            val password = findViewById<TextInputLayout>(R.id.login_password)
            val countryCodePicker = findViewById<CountryCodePicker>(R.id.country_code_picker)
            var phonenumber = phone.editText!!.text.toString().trim()
            val passwordstr = password.editText!!.text.toString().trim()
            if (phonenumber[0] == '0') {
                phonenumber = phonenumber.substring(1)
            }
            val completePhoneNumber = "${countryCodePicker.selectedCountryCodeWithPlus}$phonenumber"
            val checkBox = findViewById<CheckBox>(R.id.rememberme_checkbox)

            //Database
            val checkUser: Query =
                FirebaseDatabase.getInstance().getReference("Users").child("Accounts").orderByChild("phone")
                    .equalTo(completePhoneNumber)

            checkUser.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) = if (dataSnapshot.exists()) {
                    phone.error = null
                    phone.isErrorEnabled = false

                    val systemPassword =
                        dataSnapshot.child(completePhoneNumber).child("password")
                            .value
                    if (systemPassword!!.equals(passwordstr)) {
                        phone.error = null
                        phone.isErrorEnabled = false
                        password.error = null
                        password.isErrorEnabled = false
                        Toast.makeText(
                            this@Login,
                            "User Logged in",
                            Toast.LENGTH_SHORT
                        ).show()
                        if (!checkBox.isChecked) {
                            savedata()
                        } else {
                            rememberme()
                        }
                        callnextscreen(dataSnapshot,completePhoneNumber)

                    } else {
                        password.error = "Incorrect password"
                        dialog.dismiss()
                    }
                } else {
                    phone.error = "No user found"
                    dialog.dismiss()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@Login, error.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun callnextscreen(dataSnapshot: DataSnapshot,completePhoneNumber:String) =
        if (dataSnapshot.child(completePhoneNumber).child("bio").value!! == "Null"||dataSnapshot.child(completePhoneNumber).child("profilePicture").value!! == "Null"||dataSnapshot.child(completePhoneNumber).child("city").value!! == "Null") {
            intent = Intent(this, UpdateProfile::class.java)
            intent.putExtra("PhoneNumber",completePhoneNumber)
            startActivity(intent)
            finishAffinity()
        } else {
            intent = Intent(this, UserDashboard::class.java)
            intent.putExtra("PhoneNumber",completePhoneNumber)
            startActivity(intent)
            finishAffinity()
        }

    private fun validatefields(): Boolean {
        val phone = findViewById<TextInputLayout>(R.id.login_phone_number)
        val password = findViewById<TextInputLayout>(R.id.login_password)
        when {
            phone.editText!!.text.toString().trim().isEmpty() -> {
                phone.error = "Field can not be empty"
                return false
            }
            password.editText!!.text.toString().trim().isEmpty() -> {
                password.error = "Field can not be empty"
                return false
            }
            else -> {
                phone.error = null
                phone.isErrorEnabled = false
                password.error = null
                password.isErrorEnabled = false
                return true
            }
        }
    }

    fun callSignupScreenLogin(view: View) {
        startActivity(Intent(this, Signup::class.java))
        finish()
    }

    fun callforgetpassword(view: View) {
        val intent: Intent = Intent(this, ForgetPassword::class.java)
        startActivity(intent)
    }

    fun back(view: View) {
        finish()
    }

    fun rememberme() {
        val phone = findViewById<TextInputLayout>(R.id.login_phone_number)
        val password = findViewById<TextInputLayout>(R.id.login_password)
        val countryCodePicker = findViewById<CountryCodePicker>(R.id.country_code_picker)
        var phonenumber = phone.editText!!.text.toString().trim()
        val passwordstr = password.editText!!.text.toString().trim()
        if (phonenumber[0] == '0') {
            phonenumber = phonenumber.substring(1)
        }
        val completePhoneNumber = "${countryCodePicker.selectedCountryCodeWithPlus}$phonenumber"

        val session = sessionManager(this)
        session.setLoginContact(completePhoneNumber)
        session.setLoginPassword(passwordstr)
        session.setRemembermeStatus()
        session.setLoginStatus()
    }

    fun savedata() {
        val phone = findViewById<TextInputLayout>(R.id.login_phone_number)
        val password = findViewById<TextInputLayout>(R.id.login_password)
        val countryCodePicker = findViewById<CountryCodePicker>(R.id.country_code_picker)
        var phonenumber = phone.editText!!.text.toString().trim()
        val passwordstr = password.editText!!.text.toString().trim()
        if (phonenumber[0] == '0') {
            phonenumber = phonenumber.substring(1)
        }
        val completePhoneNumber = "${countryCodePicker.selectedCountryCodeWithPlus}$phonenumber"

        val session = sessionManager(this)
        session.apply {
            session.setLoginContact(completePhoneNumber)
            session.setLoginPassword(passwordstr)
            session.setLoginStatus()
        }
    }
}
