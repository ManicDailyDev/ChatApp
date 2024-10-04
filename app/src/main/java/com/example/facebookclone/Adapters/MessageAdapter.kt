package com.example.facebookclone.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.facebookclone.DataClasses.Message
import com.example.facebookclone.R

class MessageAdapter : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    private var messages: List<Message> = listOf()

    // This method will be used to update the list of messages
    fun updateMessages(messages: List<Message>) {
        this.messages = messages
        notifyDataSetChanged()  // This will notify the RecyclerView to update itself
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.bind(message)
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageText: TextView = itemView.findViewById(R.id.textViewMessageText)

        fun bind(message: Message) {
            messageText.text = message.text
        }
    }
}






