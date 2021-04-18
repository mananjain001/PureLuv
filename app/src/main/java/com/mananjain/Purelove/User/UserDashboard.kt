package com.mananjain.Purelove.User

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.mananjain.Purelove.Common.LoginSignup.LoginSignupStartupScreen
import com.mananjain.Purelove.Databases.sessionManager
import com.mananjain.Purelove.R

class UserDashboard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_dashboard)
        supportFragmentManager.beginTransaction()
            .replace(R.id.FLfragment, UserMatchFragment()).commit()
        val chipNavigationBar: ChipNavigationBar = findViewById(R.id.bottom_nav_menu)
        chipNavigationBar.setItemSelected(R.id.bottom_nav_match,true)
        bottomMenu()
    }

    private fun bottomMenu() {
        val chipNavigationBar: ChipNavigationBar = findViewById(R.id.bottom_nav_menu)
        chipNavigationBar.setOnItemSelectedListener { id ->
            when (id) {
                R.id.bottom_nav_match -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.FLfragment, UserMatchFragment()).commit()
                }
                R.id.bottom_nav_connect -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.FLfragment, UserChatFragment()).commit()
                }
                R.id.bottom_nav_settings -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.FLfragment, UserSettingFragment()).commit()
                }
                R.id.bottom_nav_buy -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.FLfragment, UserBuyFragment()).commit()
                }
                R.id.bottom_nav_logout -> {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                    builder.setTitle("Are you sure?")
                    builder.setMessage("Do you want to logout ").setCancelable(true)
                    builder.setPositiveButton("No") { dialogInterface: DialogInterface, i: Int -> }
                    builder.setNegativeButton("Yes") { dialogInterface: DialogInterface, i: Int ->
                        Logout()
                    }
                    builder.show()
                }
            }
        }
    }
    private fun Logout() {
        val session = sessionManager(this)
        session.removeLoginStatus()
        val intent : Intent = Intent(this, LoginSignupStartupScreen::class.java)
        startActivity(intent)
        ActivityCompat.finishAffinity(this)
    }
}