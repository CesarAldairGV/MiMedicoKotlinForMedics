package com.example.mimedicokotlinformedics.ui.chat

data class ConsultData(
    val consultId: String,
    val userName: String,
    val title: String,
    val body: String,
    val imgUrl: String,
    val userId: String,
    val medicId: String,
    val finished: Boolean
)