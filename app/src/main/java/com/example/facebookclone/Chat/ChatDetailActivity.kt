package com.example.facebookclone.Chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.facebookclone.DataClasses.Message
import com.example.facebookclone.adapter.MessageAdapter
import com.example.facebookclone.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ChatDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var messagesAdapter: MessageAdapter
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getStringExtra("userId") ?: return
        db = FirebaseFirestore.getInstance()

        binding.recyclerViewMessages.layoutManager = LinearLayoutManager(this)
        messagesAdapter = MessageAdapter() // Correctly instantiate MessageAdapter
        binding.recyclerViewMessages.adapter = messagesAdapter

        loadMessages()

        binding.buttonSendMessage.setOnClickListener {
            sendMessage()
        }
    }

    private fun loadMessages() {
        db.collection("chats").whereArrayContains("userIds", userId)
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    // Handle error
                    return@addSnapshotListener
                }

                val messages = snapshots?.toObjects(Message::class.java) ?: emptyList()
                messagesAdapter.updateMessages(messages)
            }
    }

    private fun sendMessage() {
        val text = binding.editTextMessage.text.toString()
        if (text.isBlank()) return

        val message = Message(
            senderId = FirebaseAuth.getInstance().currentUser?.uid ?: return,
            receiverId = userId,
            text = text,
            timestamp = System.currentTimeMillis()
        )

        db.collection("chats").add(message)
            .addOnSuccessListener {
                binding.editTextMessage.text.clear()
            }
            .addOnFailureListener {
                // Handle failure
            }
    }
}
