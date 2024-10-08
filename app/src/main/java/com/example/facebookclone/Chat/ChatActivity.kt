package com.example.facebookclone.Chat

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.facebookclone.DataClasses.Message
import com.example.facebookclone.Adapters.MessageAdapter
import com.example.facebookclone.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var messagesAdapter: MessageAdapter
    private lateinit var userId: String  // ID of the user you're chatting with


    private lateinit var currentUserId: String  // ID of the current user
    private lateinit var chatId: String  // Unique chat ID for the conversation
    private val realtimeDatabase = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the passed userId from Intent
        userId = intent.getStringExtra("userId") ?: ""


        // Get current user's ID
        currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return


        // Set up RecyclerView and Adapter
        binding.recyclerViewMessages.layoutManager = LinearLayoutManager(this)
        messagesAdapter = MessageAdapter()
        binding.recyclerViewMessages.adapter = messagesAdapter
        // Initialize Chat Manager to check or create a conversation
        usersPrint()
        chatManager()

        // Handle sending a message
        binding.buttonSendMessage.setOnClickListener {

            Log.d("ChatDetailActivity", "Send message button clicked")
            val messageText = binding.editTextMessage.text.toString().trim()

            if (messageText.isNotEmpty()) {
                Log.d("ChatDetailActivity", "Message text: $messageText")
                sendMessage(messageText)
            } else {
                Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun usersPrint () {


        // TODO USER 1
        //PRINTAJ IME
        println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")
        println("currentUserName")
        println()

        //PRINTAJ ID
        println("currentUserId")
        println(currentUserId)
        //USER 1 JE TRENUTNI USER KOJI JE ULOGOVAN

        // TODO USER 2

        //PRINTAJ IME
        println("username")
        println()

        //PRINTAJ ID
        println("userId")
        println(userId)

        //USER 2 JE USER NA KOG KLIKNEMO U TABELI
    }


    private fun chatManager() {
        // Generate a unique chat ID using the user IDs (sorting them ensures uniqueness)
        chatId = if (currentUserId < userId) {
            "$currentUserId-$userId"
        } else {
            "$userId-$currentUserId"
        }

        // Check if the chat exists in the "conversations" node in Realtime Database
        val chatRef = realtimeDatabase.reference.child("conversations").child(chatId)
        chatRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Chat exists, load messages
                    loadMessages(chatId)
                } else {
                    // Chat does not exist, create a new chat
                    createNewChat(chatId)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ChatDetailActivity", "Error checking chat existence: ${error.message}")
            }
        })


    }

    private fun loadMessages(chatId: String) {
        // Load messages from Firebase Realtime Database
        val messagesRef = realtimeDatabase.reference.child("messages").child(chatId)
        messagesRef.orderByChild("timestamp")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val messages = mutableListOf<Message>()
                    snapshot.children.forEach { dataSnapshot ->
                        val message = dataSnapshot.getValue(Message::class.java)
                        message?.let { messages.add(it) }
                    }

                    // Update the adapter with the loaded messages
                    messagesAdapter.updateMessages(messages)
                    if (messages.isNotEmpty()) {
                        binding.recyclerViewMessages.scrollToPosition(messages.size - 1) // Scroll to the bottom
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("ChatDetailActivity", "Error loading messages: ${error.message}")
                }
            })
        println(userId)
    }

    private fun createNewChat(chatId: String) {
        // Create a new chat in the "conversations" node with basic info
        val chatInfo = hashMapOf(
            "participants" to listOf(currentUserId, userId),
            "createdAt" to System.currentTimeMillis()
        )

        realtimeDatabase.reference.child("conversations").child(chatId)
            .setValue(chatInfo)
            .addOnSuccessListener {
                Log.d("ChatDetailActivity", "Chat created successfully")
                loadMessages(chatId) // After creating the chat, load messages
            }
            .addOnFailureListener { exception ->
                Log.e("ChatDetailActivity", "Failed to create chat: ${exception.message}")
            }
    }

    private fun sendMessage(text: String) {
        val message = Message(
            senderId = currentUserId,
            receiverId = userId,
            text = text,
            timestamp = System.currentTimeMillis()
        )

        // Save the message to the "messages" node for the specific chatId
        realtimeDatabase.reference.child("messages").child(chatId).push().setValue(message)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Clear the message input field after sending
                    binding.editTextMessage.text.clear()
                    Log.d("ChatDetailActivity", "Message sent successfully")
                } else {
                    Log.e("ChatDetailActivity", "Failed to send message: ${task.exception?.message}")
                }
            }


    }



}
