package com.mananjain.Purelove.Common.LoginSignup

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager.LayoutParams
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.mananjain.Purelove.R
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator

class LoginSignupStartupScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_login_signup_startup_screen)
        val page: RelativeLayout = findViewById(R.id.loginsignupstartuppage)
    }

    fun callLoginScreen(view: View) {
        startActivity(Intent(this, Login::class.java))
    }

    fun callSignupScreen(view: View) {
        startActivity(Intent(this, Signup::class.java))
    }
}


