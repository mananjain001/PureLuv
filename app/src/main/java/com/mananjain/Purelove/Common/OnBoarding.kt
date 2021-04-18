package com.mananjain.Purelove.Common


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.mananjain.Purelove.Common.LoginSignup.LoginSignupStartupScreen
import com.mananjain.Purelove.Databases.sessionManager
import com.mananjain.Purelove.System.OnBoarding1Fragment
import com.mananjain.Purelove.R


class OnBoarding : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_on_boarding)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_onboarding, OnBoarding1Fragment()).commit()
    }


    fun afterOnBoarding(view: View) {
        val session = sessionManager(this)
        session.setOnBoardingStatus()
        startActivity(Intent(this, LoginSignupStartupScreen::class.java))
        finish()
    }
}
