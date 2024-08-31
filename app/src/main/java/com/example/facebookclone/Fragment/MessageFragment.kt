package com.example.facebookclone.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.facebookclone.Chat.ChatDetailActivity
import com.example.facebookclone.DataClasses.User
import com.example.facebookclone.databinding.FragmentMessageBinding
import com.google.firebase.firestore.FirebaseFirestore

class MessageFragment : Fragment() {

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

        binding.recyclerViewUsers.layoutManager = LinearLayoutManager(context)
        onlineUsersAdapter = OnlineUserAdapter { user ->
            openChatWithUser(user)
        }
        binding.recyclerViewUsers.adapter = onlineUsersAdapter

        // Query Firestore for users
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                val users = result.toObjects(User::class.java)  // Ensure mapping is correct
                onlineUsersAdapter.updateUsers(users)
            }
            .addOnFailureListener { exception ->
                // Handle failure, such as showing a toast or logging the error
                Log.e("MessageFragment", "Error fetching users", exception)
            }
    }

    private fun openChatWithUser(user: User) {
        // Implement navigation logic to start a chat with the selected user
        val intent = Intent(context, ChatDetailActivity::class.java)
        intent.putExtra("userId", user.userId) // Access userId correctly
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up listeners if any
    }
}




