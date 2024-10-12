package com.example.facebookclone

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.facebookclone.DataClasses.User
import com.example.facebookclone.databinding.ActivityRegisterBinding
import com.example.facebookclone.utils.Constants
import com.example.facebookclone.utils.Extensions
import com.example.facebookclone.utils.FirebaseManager
import com.example.facebookclone.utils.PrefSingleton
import java.util.UUID


class RegisterActivity : AppCompatActivity() {
    private lateinit var prefSingleton: PrefSingleton
    private lateinit var binding: ActivityRegisterBinding
    private var TAG = "CHECKER"
    private var userToRegister = User()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefSingleton = PrefSingleton(this)

        /* button */
        binding.RegisterButton.setOnClickListener {
            if (Extensions.checker(
                    arrayListOf(
                        binding.emailEditText,
                        binding.confirmEmailEditText,
                        binding.firstNameEditText,
                        binding.lastNameEditText,
                        binding.passwordEditText,
                        binding.confirmPasswordEditText
                    ), binding.emailEditText, binding.confirmEmailEditText, this@RegisterActivity
                )
            ) {
                setupUser()
                registerNewUser(userToRegister)
            } else {
                Extensions.displayToast("nesto ne valja", this@RegisterActivity)
            }
        }

    }


    private fun registerNewUser(user: User) {
        FirebaseManager().firebaseAuth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    user.userId = task.result.user?.uid ?: UUID.randomUUID().toString()
                    FirebaseManager().registerUser(user, user.userId)
                    Log.d(TAG, "createUserWithEmail:success")
                    Toast.makeText(
                        baseContext,
                        "Authentication success.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    val intent = Intent(
                        this@RegisterActivity,
                        MainActivity::class.java
                    )
                    prefSingleton.saveString(key = Constants.USER_ID, value = task.result.user?.uid)
                    prefSingleton.saveBool(key = Constants.LOGGED_IN, value = true)
                    intent.putExtra(Constants.USER_ID, prefSingleton.getString(Constants.USER_ID))
                    startActivity(intent)
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

    private fun setupUser() {
        userToRegister.email = binding.emailEditText.text.toString()
        userToRegister.password = binding.passwordEditText.text.toString()
        userToRegister.dateofbirth =
            binding.datePicker.dayOfMonth.toString() + "/" + binding.datePicker.month.toString() + "/" + binding.datePicker.year.toString()
        userToRegister.lastName = binding.lastNameEditText.text.toString()
        userToRegister.firstName = binding.firstNameEditText.text.toString()
    }
}



