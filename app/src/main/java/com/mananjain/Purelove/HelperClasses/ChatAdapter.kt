package com.mananjain.Purelove.HelperClasses

import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mananjain.Purelove.Databases.MessageData
import com.mananjain.Purelove.R
import kotlinx.android.synthetic.main.cardview_message.view.*

class ChatAdapter(private val chatList: List<MessageData>) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mMessage: TextView = itemView.textmessage
        val mContainer:LinearLayout = itemView.messagecontainer
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.cardview_message, parent, false)
        return ChatViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val currentItem = chatList[position]
        holder.mMessage.text = currentItem.text
        if (currentItem.user)
        {
            holder.mMessage.gravity = Gravity.END
            holder.mMessage.setTextColor(Color.parseColor("#000000"))
            holder.mContainer.setBackgroundColor(Color.parseColor("#46C8BE"))
        }
        else
        {
            holder.mMessage.gravity = Gravity.START
            holder.mMessage.setTextColor(Color.parseColor("#ffffff"))
            holder.mContainer.setBackgroundColor(Color.parseColor("#9C27B0"))
        }
    }

    override fun getItemCount()=chatList.size
}