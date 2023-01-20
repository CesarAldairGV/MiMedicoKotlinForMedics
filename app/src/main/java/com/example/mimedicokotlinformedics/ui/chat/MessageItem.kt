package com.example.mimedicokotlinformedics.ui.chat

data class MessageItem(
    var message: String? = null,
    var imgUrl: String? = null,
    var photoUrl: String? = null,
    var date: String
)