package com.mananjain.Purelove.Common

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.mananjain.Purelove.Common.LoginSignup.LoginSignupStartupScreen
import com.mananjain.Purelove.Databases.sessionManager
import com.mananjain.Purelove.R
import com.mananjain.Purelove.User.UserDashboard

class SplashScreen : AppCompatActivity() {

    lateinit var backgroundImage: ImageView
    lateinit var poweredByLine: TextView
    lateinit var appName: TextView
    lateinit var sideAnim: Animation
    lateinit var bottomAnim: Animation
    val SPLASH_TIMER: Long = 4000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.splash_screen)

        val session = sessionManager(this)


        backgroundImage = findViewById(R.id.background_image)
        poweredByLine = findViewById(R.id.powered_by_line)
        appName = findViewById(R.id.splash_screen_appname)

        sideAnim = AnimationUtils.loadAnimation(this, R.anim.side_anim)
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_anim)

        backgroundImage.setAnimation(sideAnim)
        poweredByLine.setAnimation(bottomAnim)
        appName.setAnimation(sideAnim)

        Handler().postDelayed({

            if (session.getOnBoardingStatus() != 100) {
                if (session.getLoginStatus()) {
                    startActivity(Intent(this, UserDashboard::class.java))
                } else {
                    startActivity(Intent(this, LoginSignupStartupScreen::class.java))
                }
            } else {
                startActivity(Intent(this, OnBoarding::class.java))
            }
            finish()
        }, SPLASH_TIMER)

    }

}
