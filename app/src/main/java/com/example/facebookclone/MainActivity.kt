package com.example.facebookclone

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.facebookclone.Chat.ChatActivity

import com.example.facebookclone.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        binding.loginButton1.setOnClickListener {
            val username = binding.usernameEditText1.text.toString()
            val password = binding.passwordEditText1.text.toString()
            logintoaccount(username, password)
        }

        binding.registerherebutton.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)

        }
        binding.forgotpasswordbutton.setOnClickListener {
            showForgotPasswordDialog()
        }

    }

    //LogIN
    private fun logintoaccount(email: String, password: String) {
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            Toast.makeText(
                this, "Username and password cannot be empty",
                Toast.LENGTH_LONG
            ).show()
            return
        }

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
//popup
    private fun showForgotPasswordDialog() {
        val dialog = ForgotPasswordDialog(this, this)
        dialog.show()
    }


    //PASS RESET
    fun onSendEmailClicked(email: String) {
        if (auth.currentUser != null) {
            if (email.isNotEmpty()) {
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "Password reset email sent.")
                            showToast("Password reset email sent. Check your inbox.")
                        } else {
                            Log.e(TAG, "Error sending password reset email.", task.exception)
                            showToast("Failed to send password reset email. Please try again later.")
                        }
                    }
            } else {
                showToast("Please enter your email address.")
            }
        }
        else{
            showToast("Please enter your email address.")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}


