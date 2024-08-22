package com.example.facebookclone.DataClasses

data class User(
    val userId: String = "",
    val profileImageUrl: String? = null,
    val dob: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val onlineStatus: Boolean = false
)

