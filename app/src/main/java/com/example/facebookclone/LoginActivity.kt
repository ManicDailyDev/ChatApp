package com.example.facebookclone

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.facebookclone.databinding.ActivityLogInBinding
import com.example.facebookclone.utils.Constants
import com.example.facebookclone.utils.FirebaseManager
import com.example.facebookclone.utils.PrefSingleton

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogInBinding
    private lateinit var prefSingleton: PrefSingleton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefSingleton = PrefSingleton(this)

//        userLoginChecker()

        /*buttons*/
        binding.loginButton1.setOnClickListener { login() }
        binding.registerherebutton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.forgotpasswordbutton.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    /* LOG_IN */
    private fun login() {
        FirebaseManager().firebaseAuth.signInWithEmailAndPassword(
            binding.usernameEditText1.text.toString(),
            binding.passwordEditText1.text.toString()
        )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    Toast.makeText(this, "Authentication success.", Toast.LENGTH_SHORT)
                        .show()
                    prefSingleton.saveString(key = Constants.USER_ID, value = task.result.user?.uid)
                    prefSingleton.saveBool(key = Constants.LOGGED_IN, value = true)
                    navigateToMainActivity()
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    /* is_User_LoggedIN */
    private fun userLoginChecker() {
        if (prefSingleton.getBool(Constants.LOGGED_IN))
            navigateToMainActivity()
        else
            return
    }

    /* to_MainActivity */
    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(Constants.USER_ID, prefSingleton.getString(Constants.USER_ID))
        startActivity(intent)
    }
}