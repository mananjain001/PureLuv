package com.mananjain.Purelove.User

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.Fragment
import com.mananjain.Purelove.Common.LoginSignup.LoginSignupStartupScreen
import com.mananjain.Purelove.Databases.sessionManager
import com.mananjain.Purelove.R

class UserSettingFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val logoutbtn :Button = view.findViewById(R.id.logoutbtn)
        logoutbtn.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val builder: AlertDialog.Builder = AlertDialog.Builder(context!!)
                builder.setTitle("Are you sure?")
                builder.setMessage("Do you want to logout ").setCancelable(true)
                builder.setPositiveButton("No") { dialogInterface: DialogInterface, i: Int -> }
                builder.setNegativeButton("Yes") { dialogInterface: DialogInterface, i: Int ->
                    Logout()
                }
                builder.show()
            }
        })
    }

    private fun Logout() {
        val session = sessionManager(context!!)
        session.removeLoginStatus()
        val intent :Intent = Intent(activity!!,LoginSignupStartupScreen::class.java)
        startActivity(intent)
        finishAffinity(activity!!)
    }
}