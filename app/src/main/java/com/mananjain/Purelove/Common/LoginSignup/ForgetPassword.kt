package com.mananjain.Purelove.Common.LoginSignup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.*
import com.hbb20.CountryCodePicker
import com.mananjain.Purelove.R

class ForgetPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)
    }

    fun gotoOTPscreen(view: View) {
        validatefields()
    }

    fun userexist(): Boolean {
        var user:Boolean= false
        val countryCodePicker: CountryCodePicker =
            findViewById(R.id.fcountry_code_picker)
        val phoneNumber: TextInputLayout = findViewById(R.id.forget_phone_number)
        val countrycode = countryCodePicker.selectedCountryCodeAsInt
        var number = phoneNumber.editText!!.text.toString().trim()
        if (number[0] == '0') {
            number = number.substring(1)
        }
        val contact = "+" + countrycode.toString() + number

        //Database
        val checkUser: Query =
            FirebaseDatabase.getInstance().getReference("Users").orderByChild("phone")
                .equalTo(contact)
        checkUser.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ForgetPassword, error.message, Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) = if (dataSnapshot.exists()) {
                phoneNumber.error = null
                phoneNumber.isErrorEnabled = false
                callNextScreen()
                user = true
            } else {
                phoneNumber.error = "No user found"
                user = false
            }
        })
        return user
    }

    private fun callNextScreen()
    {
        val countryCodePicker: CountryCodePicker =
            findViewById(R.id.fcountry_code_picker)
        val phoneNumber: TextInputLayout = findViewById(R.id.forget_phone_number)
        val countrycode = countryCodePicker.selectedCountryCodeAsInt
        var number = phoneNumber.editText!!.text.toString().trim()
        if (number[0] == '0') {
            number = number.substring(1)
        }
        val contact = "+" + countrycode.toString() + number
        val intent = Intent(this, ForgetPass_OTP::class.java)
        intent.putExtra("PhoneNumber", contact)
        startActivity(intent)
    }

    fun back(view: View) {
        finish()
    }

    private fun validatefields(): Boolean {
        val phone = findViewById<TextInputLayout>(R.id.forget_phone_number)
        when {
            phone.editText!!.text.toString().trim().isEmpty() -> {
                phone.error = "Field can not be empty"
                return false
            }
            else -> {
                phone.error = null
                phone.isErrorEnabled = false
                userexist()
                return true
            }
        }
    }
}