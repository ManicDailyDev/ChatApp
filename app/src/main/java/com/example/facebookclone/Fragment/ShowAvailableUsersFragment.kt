package com.example.facebookclone.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.facebookclone.Chat.ChatActivity
import com.example.facebookclone.DataClasses.User
import com.example.facebookclone.databinding.FragmentMessageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class ShowAvailableUsersFragment : Fragment() {

    private lateinit var binding: FragmentMessageBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var onlineUsersAdapter: OnlineUserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMessageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val currentUserId = currentUser?.uid ?: ""
        getME(currentUserId){ it ->
            println(it?.userId)
            println(it?.lastName)
        }
        // Set up RecyclerView with the adapter
        binding.recyclerViewUsers.layoutManager = LinearLayoutManager(context)
        onlineUsersAdapter = OnlineUserAdapter { user ->
            println(user.userId)
            println(user.lastName)
        }
        binding.recyclerViewUsers.adapter = onlineUsersAdapter




        db.collection("users")
            .limit(50) // Fetch limited users for performance
            .get()
            .addOnSuccessListener { result ->
                val users = result.toObjects(User::class.java)
                val filteredUsers = users.filter { it.userId != currentUserId } // Exclude current user
                onlineUsersAdapter.updateUsers(filteredUsers)
                onlineUsersAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Error fetching users", Toast.LENGTH_SHORT).show()
                Log.e("MessageFragment", "Error fetching users", exception)
            }
    }
    fun getME(id: String, callback: (User?) -> Unit) {
        db.collection("users").document(id).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {

                val user = task.result?.toObject(User::class.java)

                callback(user)
            } else {

                callback(null)
            }
        }
    }

    private fun openChatWithUser(user: User) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val currentUserId = currentUser?.uid ?: ""

        if (currentUserId.isEmpty()) {
            Log.e("MessageFragment", "Error: current user ID is null.")
            return
        }

        // Create chatId based on current user and selected user (sorted alphabetically)
        val chatId = if (currentUserId < user.userId) {
            "${currentUserId}_${user.userId}"
        } else {
            "${user.userId}_${currentUserId}"
        }

        // Reference to the chat in Realtime Database
        val chatRef = FirebaseDatabase.getInstance().reference.child("chats").child(chatId)

        // Check if the chat already exists in Realtime Database
        chatRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                // Chat exists, navigate to the chat
                openChatDetailActivity(chatId)
            } else {
                // Chat doesn't exist, create new chat document
                createNewChat(chatId, user.userId)
            }
        }.addOnFailureListener { exception ->
            Log.e("MessageFragment", "Error checking chat existence", exception)
        }
    }

    private fun createNewChat(chatId: String, userId: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val currentUserId = currentUser?.uid ?: return

        // Reference to the "users" collection to fetch user details
        val usersRef = FirebaseFirestore.getInstance().collection("users")

        // Fetch the current user's name
        usersRef.document(currentUserId).get().addOnSuccessListener { currentUserDoc ->
            if (currentUserDoc.exists()) {
                val currentUserName = currentUserDoc.getString("firstName") ?: "Unknown User"
                Log.d("MessageFragment", "Current User Name: $currentUserName")

                // Fetch the selected user's name
                usersRef.document(userId).get().addOnSuccessListener { selectedUserDoc ->
                    if (selectedUserDoc.exists()) {
                        val selectedUserName = selectedUserDoc.getString("firstName") ?: "Unknown User"
                        Log.d("MessageFragment", "Selected User Name: $selectedUserName")

                        // Create a new chat object with both users' IDs, names, and the creation timestamp
                        val chatData = hashMapOf(
                            "chatId" to chatId,
                            "createdAt" to System.currentTimeMillis(),
                            "user1Id" to currentUserId,  // Current user's ID
                            "user2Id" to userId,         // Selected user's ID
                            "user1Name" to currentUserName,  // Current user's name
                            "user2Name" to selectedUserName, // Selected user's name
                            "firstUsername" to currentUserName,  // Storing the first user's name
                            "secondUsername" to selectedUserName // Storing the second user's name
                        )

                        // Store the chat in the Realtime Database under "chats"
                        FirebaseDatabase.getInstance().reference.child("chats").child(chatId)
                            .setValue(chatData)
                            .addOnSuccessListener {
                                // After creating the chat, open the chat detail activity
                                openChatDetailActivity(chatId)
                            }
                            .addOnFailureListener { exception ->
                                Log.e("MessageFragment", "Error creating chat", exception)
                            }
                    } else {
                        Log.e("MessageFragment", "Selected user document does not exist.")
                    }
                }.addOnFailureListener { exception ->
                    Log.e("MessageFragment", "Error fetching selected user's name", exception)
                }
            } else {
                Log.e("MessageFragment", "Current user document does not exist.")
            }
        }.addOnFailureListener { exception ->
            Log.e("MessageFragment", "Error fetching current user's name", exception)
        }
    }



    private fun openChatDetailActivity(chatId: String) {
        // Navigate to the ChatDetailActivity, passing the chatId
        val intent = Intent(context, ChatActivity::class.java).apply {
            putExtra("chatId", chatId)
        }
        startActivity(intent)
    }


    override fun onDestroyView() {
        super.onDestroyView()
    }
}

