package com.example.facebookclone

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class ForgotPasswordDialog(context: Context,
    private val listener: MainActivity) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgot_password_dialog)

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val sendEmailButton = findViewById<Button>(R.id.sendEmailButton)

        sendEmailButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            listener.onSendEmailClicked(email)
            dismiss()
        }
    }

}