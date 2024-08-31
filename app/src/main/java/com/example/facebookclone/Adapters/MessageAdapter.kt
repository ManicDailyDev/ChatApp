package com.example.facebookclone.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.facebookclone.DataClasses.Message
import com.example.facebookclone.databinding.ItemMessageBinding

class MessageAdapter(private var messages: List<Message> = listOf()) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    // Method to update the messages list
    fun updateMessages(newMessages: List<Message>) {
        messages = newMessages
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    inner class MessageViewHolder(private val binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.textViewMessageText.text = message.text // Adjust field names as per your Message data class
            // You can add more bindings here if your message layout has additional fields
        }
    }
}

