package com.mananjain.Purelove.Common.LoginSignup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.mananjain.Purelove.R
import java.util.*

class Signup2ndpage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup2ndpage)
    }

    fun callNextSignupScreen(view: View) {
        if (validategender() && validateage()) {
            val radioGroup: RadioGroup = findViewById(R.id.radiogroup)
            val datepicker: DatePicker = findViewById(R.id.age_picker)
            val selectedGender:RadioButton=findViewById(radioGroup.checkedRadioButtonId)
            val useryear = datepicker.year
            val usermonth = datepicker.month
            val userdate = datepicker.dayOfMonth
            val gender = selectedGender.text.toString()
            val dob = """$userdate/${usermonth + 1}/$useryear"""

            val name = intent.getStringExtra("Name")
            val username = intent.getStringExtra("Username")
            val email = intent.getStringExtra("Email")
            val password = intent.getStringExtra("Password")

            intent = Intent(this, Signuplastpage::class.java)

            intent.putExtra("Name",name)
            intent.putExtra("Username",username)
            intent.putExtra("Email",email)
            intent.putExtra("Password",password)
            intent.putExtra("Gender",gender)
            intent.putExtra("DOB",dob)

            startActivity(intent)
        }
    }

    fun back(view: View) {
        finish()
    }

    private fun validategender(): Boolean {
        val radioGroup: RadioGroup = findViewById(R.id.radiogroup)
        return if (radioGroup.checkedRadioButtonId == -1) {
            Toast.makeText(this, "Please Select Gender", Toast.LENGTH_SHORT).show()
            false
        } else
            true
    }

    private fun validateage(): Boolean {

        val datepicker: DatePicker = findViewById(R.id.age_picker)
        var currentyear = Calendar.getInstance().get(Calendar.YEAR)
        var currentmonth = Calendar.getInstance().get(Calendar.MONTH)
        var currentdate = Calendar.getInstance().get(Calendar.DATE)
        val useryear = datepicker.year
        val usermonth = datepicker.month
        val userdate = datepicker.dayOfMonth
        val month = arrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        if (userdate > currentdate) {
            currentdate += month[usermonth - 1]
            currentmonth = currentmonth - 1
        }

        if (usermonth > currentmonth) {
            currentyear = currentyear - 1
            currentmonth += 12
        }

        if ((currentyear - useryear) < 18) {
            Toast.makeText(this, "You are not eligible to apply ", Toast.LENGTH_SHORT).show()
            return false
        } else
            return true

    }
}