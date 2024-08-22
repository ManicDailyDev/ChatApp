package com.example.facebookclone

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.facebookclone.Chat.ChatActivity

import com.example.facebookclone.databinding.RegisterActivityBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Register : AppCompatActivity() {
    private lateinit var binding: RegisterActivityBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegisterActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()


        // "*required" text
        setupHelperTextListener(binding.emailregisterconfirm, binding.dva)
        setupHelperTextListener(binding.emailregister, binding.jedan)
        setupHelperTextListener(binding.firstname, binding.firstNameLayout)
        setupHelperTextListener(binding.lastname, binding.lastNameLayout)
        setupHelperTextListener(binding.passwordregister, binding.passwordLayout)
        setupHelperTextListener(binding.passwordregisterconfirm, binding.passwordConfirmLayout)

        binding.registerbutton.setOnClickListener {
            val username = binding.emailregister.text.toString()
            val usernameconfirm = binding.emailregisterconfirm.text.toString()
            val password = binding.passwordregister.text.toString()
            val passwordconfirm = binding.passwordregisterconfirm.text.toString()
            val firstname = binding.firstname.text.toString()
            val lastname = binding.lastname.text.toString()
            var date= binding.datePicker.dayOfMonth.toString() +"/"+ binding.datePicker.month.toString() +"/"+ binding.datePicker.year.toString()

            //test1
            Log.d(
                "Register",
                "Username: $username, Username Confirm: $usernameconfirm, " +
                        "Password: $password, Password Confirm: $passwordconfirm, " +
                        "First Name: $firstname, Last Name: $lastname, " +
                        "DOB: $date"
            )
            //test1

            if (username.isEmpty() || password.isEmpty() || usernameconfirm.isEmpty() ||
                passwordconfirm.isEmpty() || firstname.isEmpty() || lastname.isEmpty()
            ) {
                Toast.makeText(this, "Please fill out all required fields", Toast.LENGTH_LONG)
                    .show()
            } else {
                if (username == usernameconfirm && password == passwordconfirm) {
                    createAccount(username, password, firstname, lastname, date)
                } else {
                    Toast.makeText(this, "Username or password do not match", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        // "Already have an account" button
        binding.alreadyhaveaccount.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun createAccount(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        dob: String
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val user = authResult.user
                val userData = hashMapOf(
                    "firstName" to firstName,
                    "lastName" to lastName,
                    "dob" to dob
                )
                FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(user!!.uid)
                    .set(userData)
                    .addOnSuccessListener {
                        Log.d("Firebase", "Data successfully written!")
                        Toast.makeText(this, "Registration successful", Toast.LENGTH_LONG).show()
                        auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {
                                    Log.d(ContentValues.TAG, "signInWithEmail:success")
                                    Toast.makeText(
                                        baseContext, "Authentication success.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    val intent = Intent(this, ChatActivity::class.java)
                                    startActivity(intent)
                                } else {
                                    Log.w(
                                        ContentValues.TAG, "signInWithEmail:failure",
                                        task.exception
                                    )
                                    Toast.makeText(
                                        baseContext, "Authentication failed.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }

                    }
            }
            .addOnFailureListener { e ->
                // Sign up fails
                Log.e("FirebaseAuth", "Error creating user", e)
                Toast.makeText(this, "Registration Failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }


    // HelperText
    private fun setupHelperTextListener(
        editText: TextInputEditText,
        textInputLayout: TextInputLayout
    ) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed
            }

            override fun afterTextChanged(s: Editable?) {
                // If the text is empty, show the helper text, otherwise hide it
                textInputLayout.helperText = if (s.isNullOrEmpty()) "*required" else null
            }
        })
    }
}
