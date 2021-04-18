package com.mananjain.Purelove.User

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mananjain.Purelove.Common.Chat
import com.mananjain.Purelove.Databases.User
import com.mananjain.Purelove.Databases.sessionManager
import com.mananjain.Purelove.HelperClasses.OnChatClickListener
import com.mananjain.Purelove.HelperClasses.UsersAdapter
import com.mananjain.Purelove.R
import kotlinx.android.synthetic.main.fragment_user_chat.*


class UserChatFragment : Fragment(),OnChatClickListener {

    val list: MutableList<User> = mutableListOf()
    lateinit var mobileno: String
    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val session = sessionManager(context!!)
        mobileno = session.getLoginContact()
        builder = AlertDialog.Builder(context!!)
        builder.setCancelable(false)
        builder.setView(R.layout.progress_bar_layout)
        dialog = builder.create()
        dialog.show()
        fetchResultsAndInitializeAdapter()
        Chat_recyclerview.hasFixedSize()
        Chat_recyclerview.adapter = UsersAdapter(list, this)
        Chat_recyclerview.layoutManager = LinearLayoutManager(context)
    }

    private fun fetchResultsAndInitializeAdapter() {

        val databaseReference =
            FirebaseDatabase.getInstance().reference.child("Users").child("Connections")
                .child(mobileno)
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (data: DataSnapshot in snapshot.children) {
                        val userReference = FirebaseDatabase.getInstance().reference.child("Users")
                            .child("Accounts").child(data.key!!)
                        userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    val item = User(
                                        snapshot.child("name").value.toString(),
                                        snapshot.child("profilePicture").value.toString(),
                                        snapshot.child("phone").value.toString()
                                    )
                                    list.add(item)
                                    Chat_recyclerview.adapter!!.notifyDataSetChanged()
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                    dialog.dismiss()
                } else {
                    dialog.dismiss()
                    Toast.makeText(context, "You are not matched with anyone", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        })

    }

    override fun onItemClick(item: User, position: Int) {
        try {
            val intent = Intent(context, Chat::class.java)
            intent.putExtra("Name", item.name)
            intent.putExtra("MobileNumber", item.mobilenumber)
            intent.putExtra("ProfilePictureUrl", item.profileimageurl)
            val databaseReference =
                FirebaseDatabase.getInstance().reference.child("Users").child("Connections")
                    .child(mobileno).child(item.mobilenumber!!)
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        intent.putExtra("Chatid", snapshot.child("ChatId").value.toString())
                        startActivity(intent)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                }
            })

        }
        catch (e:Exception)
        {
            Toast.makeText(context,"Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }
}