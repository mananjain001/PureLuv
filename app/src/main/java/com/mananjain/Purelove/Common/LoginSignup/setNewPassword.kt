package com.mananjain.Purelove.Common.LoginSignup

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.mananjain.Purelove.R
import java.util.regex.Pattern

class setNewPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_set_new_password)
    }

    fun updatePassword(view: View) {
        validatefields()
    }

    private fun callnextscreen() {
        val phonenumber = intent.getStringExtra("PhoneNumber")
        val pass1 = findViewById<TextInputLayout>(R.id.new_pass)
        val password = pass1.editText!!.text.toString().trim()

        val referance: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")
        referance.child(phonenumber).child("password").setValue(password)
        startActivity(Intent(this@setNewPassword, forgetPasswordSuccessMsg::class.java))
        finishAffinity()
    }

    private fun validatefields(): Boolean {

        val pass1 = findViewById<TextInputLayout>(R.id.new_pass)
        val pass2 = findViewById<TextInputLayout>(R.id.new_pass_2)
        when {
            pass1.editText!!.text.toString().trim().isEmpty() -> {
                pass1.error = "Field can not be empty"
                return false
            }
            pass2.editText!!.text.toString().trim().isEmpty() -> {
                pass2.error = "Field can not be empty"
                return false
            }
            !pass1.editText!!.text.toString().trim()
                .equals(pass2.editText!!.text.toString().trim()) -> {
                pass2.error = "Passwords do not match"
                return false
            }
            else -> {
                pass1.error = null
                pass1.isErrorEnabled = false
                pass2.error = null
                pass2.isErrorEnabled = false
                validatepassword()
                return true
            }
        }
    }

    private fun validatepassword(): Boolean {

        val password: TextInputLayout = findViewById(R.id.new_pass)
        val str: String = password.editText!!.text.toString().trim()

        when {

            str.length > 25 -> {
                password.error = "Password is too large! "
                return false
            }
            str.length < 8 -> {
                password.error = "Password should have atleast 8 characters "
                return false
            }
            else -> {
                var exp = ".*[0-9].*"
                var pattern = Pattern.compile(exp)
                var matcher = pattern.matcher(str)
                if (!matcher.matches()) {
                    password.error = "Password should contain atleast 1 digit"
                    return false
                }

                exp = ".*[a-z].*"
                pattern = Pattern.compile(exp)
                matcher = pattern.matcher(str)
                if (!matcher.matches()) {
                    password.error = "Password should contain atleast 1 small letter"
                    return false
                }

                exp = ".*[A-Z].*"
                pattern = Pattern.compile(exp)
                matcher = pattern.matcher(str)
                if (!matcher.matches()) {
                    password.error = "Password should contain atleast 1 capital letter"
                    return false
                }

                exp = ".*[~!@#\$%^&*()\\-=+|\\[{\\]};:'\",<.>/?].*"
                pattern = Pattern.compile(exp)
                matcher = pattern.matcher(str)
                if (!matcher.matches()) {
                    password.error = "Password should contain atleast 1 special letter"
                    return false
                }

                password.error = null
                password.isErrorEnabled = false
                callnextscreen()
                return true
            }
        }


    }
}