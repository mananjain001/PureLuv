package com.mananjain.Purelove.Common.LoginSignup

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.mananjain.Purelove.R
import com.mananjain.Purelove.R.layout.activity_signup
import java.util.regex.Pattern

class Signup : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(activity_signup)
    }

    fun callNextSignupScreen(view: View) {


        if (validateFullName() && validateUsername() && validateemail() && validatepassword()) {
            intent = Intent(this, Signup2ndpage::class.java)

            val name: TextInputLayout = findViewById(R.id.fullname)
            var str: String = name.editText!!.text.toString().trim()
            str.capitalize()
            intent.putExtra("Name", str)

            val username: TextInputLayout = findViewById(R.id.username)
            str = username.editText!!.text.toString().trim()
            intent.putExtra("Username", str)

            val email: TextInputLayout = findViewById(R.id.email)
            str = email.editText!!.text.toString().trim()
            intent.putExtra("Email", str)

            val password: TextInputLayout = findViewById(R.id.password)
            str = password.editText!!.text.toString().trim()
            intent.putExtra("Password", str)

            startActivity(intent)
        }
    }

    fun callLoginScreenSignup(view: View) {
        startActivity(Intent(this, Login::class.java))
        finish()
    }

    fun back(view: View) {
        finish()
    }

    private fun validateFullName(): Boolean {

        val name: TextInputLayout = findViewById(R.id.fullname)
        val str: String = name.editText!!.text.toString().trim()

        return if (str.isEmpty()) {
            name.error = "Field can not be empty"
            false
        } else {
            name.error = null
            name.isErrorEnabled = false
            true
        }
    }

    private fun validateUsername(): Boolean {

        val username: TextInputLayout = findViewById(R.id.username)
        val str: String = username.editText!!.text.toString().trim()
        val exp = ".*[~!@#\$%^&*()\\-=+|\\[{\\]};:'\",<.>/?].*"
        val pattern = Pattern.compile(exp)
        val matcher = pattern.matcher(str)


        when {
            str.isEmpty() -> {
                username.error = "Field can not be empty"
                return false
            }
            str.length < 5 -> {
                username.error = "Username is too small! "
                return false
            }
            str.length > 20 -> {
                username.error = "Username is too large! "
                return false
            }
            matcher.matches() -> {
                username.error = "Username can not contain special characters "
                return false
            }
            str.contains(" ") -> {
                username.error = "Username can not contain blank spaces "
                return false
            }
            else -> {
                username.error = null
                username.isErrorEnabled = false
                return true
            }
        }
    }

    private fun validateemail(): Boolean {

        val email: TextInputLayout = findViewById(R.id.email)
        val str: String = email.editText!!.text.toString().trim()
        val exp = "^[a-zA-Z0-9._-]+@+[a-zA-Z0-9]+\\.+[A-Za-z]{2,}$"
        val pattern = Pattern.compile(exp)
        val matcher = pattern.matcher(str)


        if (str.isEmpty()) {
            email.error = "Field can not be empty"
            return false
        } else
            if (!matcher.matches()) {
                email.error = "Enter a valid Email"
                return false
            } else {
                email.error = null
                email.isErrorEnabled = false
                return true
            }
    }

    private fun validatepassword(): Boolean {

        val password: TextInputLayout = findViewById(R.id.password)
        val str: String = password.editText!!.text.toString().trim()

        when {
            str.isEmpty() -> {
                password.error = "Field can not be empty"
                return false
            }
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
                return true
            }
        }

    }


}
