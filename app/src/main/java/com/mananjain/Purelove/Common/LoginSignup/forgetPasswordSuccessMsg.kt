package com.mananjain.Purelove.Common.LoginSignup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.mananjain.Purelove.R

class forgetPasswordSuccessMsg : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_forget_password_success_msg)
    }

    fun back(view: View) {
        startActivity(
            Intent(this@forgetPasswordSuccessMsg, LoginSignupStartupScreen::class.java)
        )
        finishAffinity()
    }
    fun callLoginScreen(view: View)
    {
        startActivity(Intent(this@forgetPasswordSuccessMsg, Login::class.java))
        finishAffinity()
    }
}