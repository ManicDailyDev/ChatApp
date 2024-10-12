package com.example.facebookclone.utils

import android.app.Activity
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

object Extensions {
    fun isMailPasswordEmpty(editText: EditText, emailconfirmEditText: TextInputEditText): String {
        return if (editText.text.isNotEmpty() && editText.text.toString()
                .count() >= 6 && editText.text.toString().count() <= 25
        )
            editText.text.toString().trim { it <= ' ' }
        else
            "error"
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isNameFL(editText: EditText, lastNameEditText: TextInputEditText): String {
        return if (editText.text.isNotEmpty() && editText.text.toString()
                .count() >= 3 && editText.text.toString().count() <= 25
        )
            editText.text.toString()
        else
            "error"
    }

    private fun equalsEditText(editText1: EditText, editText2: EditText): Boolean {
        return editText1.text.toString() == editText2.text.toString()
    }

     fun checker(editTexts:ArrayList<EditText>,emailET:EditText,confirmEmailET:EditText,context: Activity): Boolean {

        if ((editTexts.any{it.text.isEmpty()} || editTexts.any { it.text.toString().count() < 6 }))
        {
            displayToast("MORA BUDE DUZE OD 6",context)
            return  false
        }
        else {

            if (isEmailValid(emailET.text.toString())) {

                if (equalsEditText(emailET,confirmEmailET)){
                    return true
                } else {
                    displayToast("NE POKLAPA SE MAIL I CONFIRM",context)
                    return false
                }

            } else {
                displayToast("EMAIL NJE VALIDAN",context)
                return false
            }
        }

    }

     fun displayToast(message:String,context:Activity){
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show()
    }
}



