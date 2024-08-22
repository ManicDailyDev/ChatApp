package com.example.facebookclone.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.facebookclone.DataClasses.Message
import com.example.facebookclone.R

class ChatAdapter : RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

    private var messages: List<Message> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    fun updateMessages(messages: List<Message>) {
        this.messages = messages
        notifyDataSetChanged()
    }

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(message: Message) {
            itemView.findViewById<TextView>(R.id.textViewMessageText).text = message.messageText
            // Set other message details as needed
        }
    }
}
