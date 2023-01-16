package com.example.mimedicokotlinformedics.ui.petitions

data class PetitionItem (
    val petitionId: String,
    val subject: String,
    val date: String,
    val body: String,
    val urlPhoto: String? = null
)