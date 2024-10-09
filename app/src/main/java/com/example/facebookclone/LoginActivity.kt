package com.example.facebookclone

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import androidx.appcompat.app.AppCompatActivity
import com.example.facebookclone.databinding.ActivityLogInBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogInBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* firebase */
        auth = FirebaseAuth.getInstance()

        /* bindings */
        binding.usernameEditText1.text.toString()
        binding.passwordEditText1.text.toString()

        /* buttons */
        binding.loginButton1.setOnClickListener {
            login()
        }

        binding.registerherebutton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.forgotpasswordbutton

    }

    /* LOG_IN */
    private fun login() {
        val email = binding.usernameEditText1.text.toString()
        val password = binding.passwordEditText1.text.toString()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    Log.d(TAG, "signInWithEmail:success")
                    Toast.makeText(baseContext, "Authentication success.", Toast.LENGTH_SHORT)
                        .show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT)
                        .show()
                }
            }

    }
}