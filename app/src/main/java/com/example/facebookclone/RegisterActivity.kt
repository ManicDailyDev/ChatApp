package com.example.facebookclone

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.facebookclone.databinding.ActivityRegisterBinding
import androidx.appcompat.app.AppCompatActivity
import com.example.facebookclone.DataClasses.User
import com.example.facebookclone.utils.Extensions
import com.example.facebookclone.utils.FirebaseManager


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* button */
        binding.RegisterButton.setOnClickListener {
            registerNewUser()
        }

    }

    private fun registerNewUser() {
        checker()
        val userToRegister = User()


        FirebaseManager().firebaseAuth.createUserWithEmailAndPassword(
            userToRegister.(this
            ) { task ->
                if (task.isSuccessful) {
                    {
                    }
                    userToRegister.dateofbirth =
                        binding.DatePicker.dayOfMonth.toString() + "/" + binding.DatePicker.month.toString() + "/" + binding.DatePicker.year.toString()
                    FirebaseManager().registerUser(userToRegister, task.result.user?.uid ?: "")
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = FirebaseManager().firebaseAuth.currentUser
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




    fun checker() {
        Extensions.isMailPasswordEmpty(
            binding.emailEditText,
            binding.emailconfirmEditText
        )
        Extensions.isValidEmail(
            binding.emailEditText.toString(),
            binding.emailconfirmEditText.toString()
        )
        Extensions.isNameFL(
            binding.FirstNameEditText,
            binding.LastNameEditText
        )
        Extensions.equalsEditText(
            binding.emailEditText,
            binding.emailconfirmEditText
        )
        Extensions.equalsEditText(
            binding.PasswordRegisterEditText,
            binding.PasswordConfirmRegisterEditText
        )
    }

}



