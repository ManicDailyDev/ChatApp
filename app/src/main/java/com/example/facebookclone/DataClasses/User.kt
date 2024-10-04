package com.example.facebookclone.DataClasses


data class User(
    val userId: String = "",
    val profileImageUrl: String? = null,  // URL of the profile image
    val dob: String = "",  // Date of birth
    val firstName: String = "",
    val lastName: String = "",
    val onlineStatus: Boolean = false  // Online status (true/false)
)

