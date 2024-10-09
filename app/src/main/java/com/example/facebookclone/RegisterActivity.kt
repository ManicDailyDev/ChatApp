package com.example.facebookclone

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.facebookclone.databinding.ActivityRegisterBinding
import androidx.appcompat.app.AppCompatActivity
import com.example.facebookclone.DataClasses.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private var mFirebaseAuth = FirebaseAuth.getInstance()
    private var firebaseStore = FirebaseFirestore.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* firebase */
        auth = Firebase.auth

        /* button */
        binding.registerbutton.setOnClickListener {
        registernewuser()
        }

}

private fun registernewuser() {

    val userToRegister:User = User()

    auth.createUserWithEmailAndPassword(binding.emailregister.text.toString(), binding.passwordregister.text.toString())
        .addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                userToRegister.email = binding.emailregister.text.toString()
                userToRegister.lastName = binding.lastname.text.toString()
                userToRegister.firstName = binding.firstname.text.toString()
                userToRegister.dateofbirth = binding.datePicker.dayOfMonth.toString() + "/" + binding.datePicker.month.toString() + "/" + binding.datePicker.year.toString()
               registerUser(userToRegister,task.result.user?.uid ?: "")
                Log.d(TAG, "createUserWithEmail:success")
                val user = auth.currentUser
                Toast.makeText(
                    baseContext,
                    "Authentication success.",
                    Toast.LENGTH_SHORT,
                ).show()
            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                Toast.makeText(
                    baseContext,
                    "Authentication failed.",
                    Toast.LENGTH_SHORT,
                ).show()

            }
        }
}

    private   fun registerUser(user:User,id:String){
        firebaseStore.collection("users").document(id).set(user)
    }
}



