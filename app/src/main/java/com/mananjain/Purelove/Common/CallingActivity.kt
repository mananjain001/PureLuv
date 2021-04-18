package com.mananjain.Purelove.Common

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mananjain.Purelove.R
import com.squareup.picasso.Picasso
import com.twilio.Twilio
import com.twilio.rest.video.v1.Room
import kotlinx.android.synthetic.main.activity_calling.*

class CallingActivity : AppCompatActivity() {
    lateinit var name:String
    lateinit var profilepicture:String
    val Account_SID:String = "SK05ce0f476b7681d0c15edae86ddf36ae"
    val Auth_Secret:String = "iMPkSfNwBEJwZ7TNlrAJdPEOOqz3hb41"
    lateinit var Path_SID:String


    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_calling)
            name = intent.getStringExtra("Name")
            profilepicture = intent.getStringExtra("ProfilePictureUrl")
            Picasso.get().load(profilepicture).fit().into(calling_image)
            calling_name.text = name
            Twilio.init(Account_SID, Auth_Secret)
            val room = Room.creator().setUniqueName("Myroom").create()
            Path_SID = room.sid
        }
        catch (e:Exception)
        {
            Toast.makeText(this,"oops! Something went wrong",Toast.LENGTH_SHORT).show()
            finish()
        }
    }
    fun endcall(view: View)
    {
        Room.updater(Path_SID,Room.RoomStatus.COMPLETED)
        finish()
    }
}