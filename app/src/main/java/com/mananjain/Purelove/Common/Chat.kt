package com.mananjain.Purelove.Common

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.mananjain.Purelove.Databases.ChatData
import com.mananjain.Purelove.Databases.MessageData
import com.mananjain.Purelove.Databases.sessionManager
import com.mananjain.Purelove.HelperClasses.ChatAdapter
import com.mananjain.Purelove.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_chat.*

class Chat : AppCompatActivity() {

    lateinit var mynumber: String
    lateinit var chatId: String
    lateinit var chatReference: DatabaseReference
    lateinit var name:String
    lateinit var profilepicture:String
    lateinit var mobilenumber:String

    val messageList: MutableList<MessageData> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        val session = sessionManager(this)
        mynumber = session.getLoginContact()
        name = intent.getStringExtra("Name")
        mobilenumber = intent.getStringExtra("MobileNumber")
        profilepicture = intent.getStringExtra("ProfilePictureUrl")
        chatId = intent.getStringExtra("Chatid")
        chatname.text = name
        Picasso.get().load(profilepicture).into(chatprofilepicture)
        CreatechatReferance()
        Message_recyclerview.adapter = ChatAdapter(messageList)
        Message_recyclerview.layoutManager = LinearLayoutManager(this)
    }

    private fun CreatechatReferance() {
        chatReference =
            FirebaseDatabase.getInstance().getReference().child("Users").child("Chat").child(chatId)
        getOldMessages()
    }

    private fun getOldMessages() {
        chatReference.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshotmain: DataSnapshot) {
                for (snapshot: DataSnapshot in snapshotmain.children) {
                    if (snapshot.exists()) {
                        val message = snapshot.child("text").value.toString()
                        val sentBy = snapshot.child("user").value.toString()
                        val currentUser = sentBy == mynumber
                        val messageData = MessageData(
                            message,
                            currentUser
                        )
                        messageList.add(messageData)
                        Message_recyclerview.adapter!!.notifyDataSetChanged()
                    }
                }
                Message_recyclerview.layoutManager!!.scrollToPosition(messageList.size - 1)

            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun newMessagereceived() {
        chatReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshotmain: DataSnapshot, previousChildName: String?) {
                messageList.clear()
                getOldMessages()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun back(view: View) {
        finish()
    }

    fun sendMessage(view: View) {
        val sendMessageText = message.text.toString()

        if (!sendMessageText.isEmpty()) {
            val newMessageId: DatabaseReference = chatReference.push()
            val chatData = ChatData(
                sendMessageText,
                mynumber
            )
            newMessageId.setValue(chatData)
        }
        message.text = null
        messageList.clear()
    }
    fun Call(view:View)
    {
        intent = Intent(this,CallingActivity::class.java)
        intent.putExtra("Name",name)
        intent.putExtra("MobileNumber",mobilenumber)
        intent.putExtra("ProfilePictureUrl",profilepicture)
        startActivity(intent)
    }
}