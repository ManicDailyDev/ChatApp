package com.example.facebookclone

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.facebookclone.databinding.ActivityForgotPasswordBinding
import com.example.facebookclone.utils.Extensions
import com.example.facebookclone.utils.FirebaseManager


class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.forgotPasswordButton.setOnClickListener { beginRecovery(binding.forgotPasswordEditText.text.toString()) }
    }

    fun beginRecovery(email: String?) {
        FirebaseManager().firebaseAuth.fetchSignInMethodsForEmail(email!!)
            .addOnCompleteListener { checkTask ->
                if (checkTask.isSuccessful) {
                    FirebaseManager().firebaseAuth.sendPasswordResetEmail(binding.forgotPasswordEditText.text.toString())
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG, "forgot password:success")
                                Toast.makeText(
                                    this,
                                    "Reset password e-mail sent.",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            } else {
                                Log.w(TAG, "forgot password:failure", task.exception)
                                Toast.makeText(this, "Email don't exist", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                } else {
                    Extensions.displayToast("No Mail in our Database", this)
                }
            }
    }
}
