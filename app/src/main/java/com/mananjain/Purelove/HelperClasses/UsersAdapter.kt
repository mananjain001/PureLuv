package com.mananjain.Purelove.HelperClasses

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mananjain.Purelove.Databases.User
import com.mananjain.Purelove.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.cardview_chat.view.*

class UsersAdapter(private val userList: List<User>, var clickListener: OnChatClickListener) :
    RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mName: TextView = itemView.cardname
        val Number: TextView = itemView.number
        val mImage: CircleImageView = itemView.cardprofilepicture

        fun initialize(item:User,action:OnChatClickListener)
        {
            mName.text= item.name
            Number.text = item.mobilenumber
            Picasso.get().load(item.profileimageurl).into(mImage)
            itemView.setOnClickListener{
                action.onItemClick(item,adapterPosition)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.cardview_chat, parent, false)
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.initialize(userList.get(position),clickListener)
    }

    override fun getItemCount()=userList.size
}

interface OnChatClickListener{
    fun onItemClick(item:User , position: Int)
}