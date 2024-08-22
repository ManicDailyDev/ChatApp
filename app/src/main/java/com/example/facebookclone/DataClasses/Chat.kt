package com.example.facebookclone.DataClasses

data class Chat(
    val chatId: String = "",
    val userId: String = "",
    val messages: List<Message> = listOf()
)