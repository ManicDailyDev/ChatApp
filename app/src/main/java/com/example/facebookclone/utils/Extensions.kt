package com.example.facebookclone.utils

import android.widget.EditText
import com.google.android.material.textfield.TextInputEditText
import java.util.regex.Pattern

object Extensions {
    fun isMailPasswordEmpty(editText: EditText, emailconfirmEditText: TextInputEditText): String {
        return if (editText.text.isNotEmpty() && editText.text.toString()
                .count() >= 6 && editText.text.toString().count() <= 25
        )
            editText.text.toString().trim { it <= ' ' }
        else
            "error"
    }

    fun isValidEmail(email: CharSequence, toString: String): Boolean {
        var isValid = true
        val expression = "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)
        if (!matcher.matches()) {
            isValid = false
        }
        return isValid
    }

    fun isNameFL(editText: EditText, lastNameEditText: TextInputEditText): String {
        return if (editText.text.isNotEmpty() && editText.text.toString()
                .count() >= 3 && editText.text.toString().count() <= 25
        )
            editText.text.toString()
        else
            "error"
    }

    fun equalsEditText(editText1: EditText,editText2: EditText): String {
        return if (editText1.text.toString() == editText2.text.toString()) {
            equalsEditText(editText1, editText2)
        } else {
            "error"
        }
    }
}



