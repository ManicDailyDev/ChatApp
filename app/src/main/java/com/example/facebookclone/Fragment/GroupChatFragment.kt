package com.example.facebookclone.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.facebookclone.databinding.FragmentGroupChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class GroupChatFragment : Fragment() {

    private lateinit var binding: FragmentGroupChatBinding
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroupChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = FirebaseFirestore.getInstance()

        binding.buttonCreateGroup.setOnClickListener {
            val groupName = binding.editTextGroupName.text.toString().trim()
            if (groupName.isNotEmpty()) {
                createGroup(groupName)
            } else {
                Toast.makeText(requireContext(), "Group name cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createGroup(groupName: String) {
        val user = FirebaseAuth.getInstance().currentUser
        val groupId = db.collection("groups").document().id

        val groupData = hashMapOf(
            "id" to groupId,
            "name" to groupName,
            "createdBy" to user?.uid,
            "members" to listOf(user?.uid),
            "createdAt" to System.currentTimeMillis()
        )

        db.collection("groups").document(groupId).set(groupData)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Group created successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Failed to create group: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
