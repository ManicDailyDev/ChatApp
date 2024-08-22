package com.example.facebookclone.DataClasses

data class Message(
    val senderId: String = "",
    val receiverId: String = "",
    val text: String = "",
    val timestamp: Long = 0L,
    val type: String = "text",
    val isRead: Boolean = false,
    val isEdited: Boolean = false,
    val isDeleted: Boolean = false
)