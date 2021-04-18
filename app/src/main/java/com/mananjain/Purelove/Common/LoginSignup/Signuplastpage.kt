package com.mananjain.Purelove.Common.LoginSignup

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.*
import com.hbb20.CountryCodePicker
import com.mananjain.Purelove.R
import java.util.regex.Pattern

class Signuplastpage : AppCompatActivity() {

    lateinit var name: String
    lateinit var username: String
    lateinit var email: String
    lateinit var password: String
    lateinit var gender: String
    lateinit var dob: String
    lateinit var builder: AlertDialog.Builder
    lateinit var dialog : AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signuplastpage)

        builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setView(R.layout.progress_bar_layout)
        dialog = builder.create()
        name = intent.getStringExtra("Name")
        username = intent.getStringExtra("Username")
        email = intent.getStringExtra("Email")
        password = intent.getStringExtra("Password")
        gender = intent.getStringExtra("Gender")
        dob = intent.getStringExtra("DOB")
    }

    fun callVerifyOTPScreen(view: View) {

        showCustomDialog()
    }

    private fun callNextScreen(){
                val countryCodePicker: CountryCodePicker =
                    findViewById(R.id.scountry_code_picker)
                val phoneNumber: TextInputLayout = findViewById(R.id.signup_phone_number)
                val countrycode = countryCodePicker.selectedCountryCodeAsInt
                var number = phoneNumber.editText!!.text.toString().trim()
                if (number[0] == '0') {
                    number = number.substring(1)
                }
                val contact = "+" + countrycode.toString() + number
                intent = Intent(this, VerifyOTP::class.java)
                intent.putExtra("Name", name)
                intent.putExtra("Username", username)
                intent.putExtra("Email", email)
                intent.putExtra("Password", password)
                intent.putExtra("Gender", gender)
                intent.putExtra("DOB", dob)
                intent.putExtra("PhoneNumber", contact)
                startActivity(intent)
                finishAffinity()
    }
    private fun showCustomDialog() {
        val countryCodePicker: CountryCodePicker = findViewById(R.id.scountry_code_picker)
        val phoneNumber: TextInputLayout = findViewById(R.id.signup_phone_number)
        val countrycode = countryCodePicker.selectedCountryCodeAsInt
        var number = phoneNumber.editText!!.text.toString().trim()
        if (number[0] == '0') {
            number = number.substring(1)
        }
        val contact = "+"+countrycode.toString()+number
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Are you sure?")
        builder.setMessage("Do you want to sent OTP on $contact").setCancelable(true)
        builder.setPositiveButton("Ok") { dialogInterface: DialogInterface, i: Int ->
            verifyPhoneNumber()
        }
        builder.setNegativeButton("Edit") { dialogInterface: DialogInterface, i: Int -> }
        builder.show()

    }
    fun back(view: View) {
        finish()
    }

    fun userexist(): Boolean {
        dialog.show()
        val countryCodePicker: CountryCodePicker = findViewById(R.id.scountry_code_picker)
        val phoneNumber: TextInputLayout = findViewById(R.id.signup_phone_number)
        val countrycode = countryCodePicker.selectedCountryCodeAsInt
        var number = phoneNumber.editText!!.text.toString().trim()
        if (number[0] == '0') {
            number = number.substring(1)
        }
        val contact = "+"+countrycode.toString()+number
        var user: Boolean = false

        //Database
        val checkUser: Query =
            FirebaseDatabase.getInstance().getReference("Users").orderByChild("phone")
                .equalTo(contact)
        checkUser.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) = if (dataSnapshot.exists()) {
                phoneNumber.error = "user already exist"
                dialog.dismiss()
                user = false
            } else {
                phoneNumber.error = null
                phoneNumber.isErrorEnabled = false
                callNextScreen()
                user = true
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Signuplastpage, error.message, Toast.LENGTH_SHORT).show()
            }

        })
        return user
    }

    fun verifyPhoneNumber(): Boolean {
        val phoneNumber: TextInputLayout = findViewById(R.id.signup_phone_number)
        var str: String = phoneNumber.editText!!.text.toString().trim()
        if (str[0] == '0') {
            str = str.substring(1)
        }
        val exp = ".*[~!@#\$%^&*()\\-=+|\\[{\\]};:'\",<.>/?].*"
        val pattern = Pattern.compile(exp)
        val matcher = pattern.matcher(str)


        if (str.isEmpty()) {
            phoneNumber.error = "Field can not be empty"
            return false
        } else
            if (matcher.matches()) {
                phoneNumber.error = "Detail can not contain special characters"
                return false
            } else
                if (str.contains(" ")) {
                    phoneNumber.error = "Detail can not contain blank spaces "
                    return false
                } else {
                    phoneNumber.error = null
                    phoneNumber.isErrorEnabled = false
                    userexist()
                    return true
                }
    }
}