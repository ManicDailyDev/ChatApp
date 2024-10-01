package com.example.facebookclone.Fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.facebookclone.R
import com.example.facebookclone.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream


class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var storageReference: StorageReference
    private val PICK_IMAGE_REQUEST = 1
    private val MAX_IMAGE_SIZE = 1 * 1024 * 1024 // 1 MB
    private val MAX_NAME_LENGTH = 20

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
        storageReference = FirebaseStorage.getInstance().reference

        binding.imageViewProfile.setOnClickListener {
            openImagePicker()
        }

        val user = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection("users").document(user?.uid ?: "")

        userRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val userData = document.data
                    val firstName = userData?.get("firstName") as? String ?: ""
                    val lastName = userData?.get("lastName") as? String ?: ""
                    val dob = userData?.get("dob") as? String ?: ""
                    val profileImageUrl = userData?.get("profileImageUrl") as? String

                    binding.textViewFirstName.text = firstName
                    binding.textViewLastName.text = lastName
                    binding.textViewDateOfBirth.text = dob

                    profileImageUrl?.let {
                        Glide.with(this).load(it).into(binding.imageViewProfile)
                    }
                } else {
                    Log.d(TAG, "User data not found")
                }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error getting user data", e)
            }

        // Add click listeners for editable fields
        binding.textViewFirstName.setOnClickListener {
            showEditDialog("First Name", binding.textViewFirstName.text.toString())
        }
        binding.textViewLastName.setOnClickListener {
            showEditDialog("Last Name", binding.textViewLastName.text.toString())
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                resizeImageAndUpload(uri)
            }
        }
    }

    private fun resizeImageAndUpload(uri: Uri) {
        val user = FirebaseAuth.getInstance().currentUser ?: return
        val storageRef = storageReference.child("profile_images/${user.uid}.jpg")
        val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
        val data = baos.toByteArray()

        if (data.size > MAX_IMAGE_SIZE) {
            Toast.makeText(requireContext(), "Image is too large. Please select a smaller image.", Toast.LENGTH_SHORT).show()
            return
        }

        val uploadTask = storageRef.putBytes(data)
        uploadTask.addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                updateProfileImageUrl(downloadUri.toString())
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Failed to upload image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateProfileImageUrl(url: String) {
        val user = FirebaseAuth.getInstance().currentUser ?: return
        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection("users").document(user.uid)

        userRef.update("profileImageUrl", url)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Profile image updated", Toast.LENGTH_SHORT).show()
                Glide.with(this).load(url).into(binding.imageViewProfile)
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to update profile image", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showEditDialog(field: String, currentValue: String) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_text, null)
        val input = dialogView.findViewById<EditText>(R.id.editTextValue)
        input.setText(currentValue)

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogView)
        builder.setTitle("Edit $field")

        builder.setPositiveButton("Save") { dialog, _ ->
            val newValue = input.text.toString()
            if (newValue.length > MAX_NAME_LENGTH) {
                Toast.makeText(requireContext(), "Maximum $field length exceeded", Toast.LENGTH_SHORT).show()
            } else {
                updateUserField(field, newValue)
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        val alertDialog = builder.create()
        alertDialog.window?.setBackgroundDrawableResource(R.drawable.edittext_background)
        alertDialog.show()
    }

    private fun updateUserField(field: String, value: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val updates = when (field) {
            "First Name" -> mapOf("firstName" to value)
            "Last Name" -> mapOf("lastName" to value)
            "Date of Birth" -> mapOf("dob" to value)
            else -> return
        }

        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection("users").document(userId)

        userRef.update(updates)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "$field updated", Toast.LENGTH_SHORT).show()
                when (field) {
                    "First Name" -> binding.textViewFirstName.text = value
                    "Last Name" -> binding.textViewLastName.text = value
                    "Date of Birth" -> binding.textViewDateOfBirth.text = value
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to update $field", Toast.LENGTH_SHORT).show()
            }
    }

    companion object {
        private const val TAG = "ProfileFragment"
        private const val IMAGE_PICK_REQUEST = 1
    }
}


