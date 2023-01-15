package com.example.mimedicokotlinformedics.ui.signuptwo

data class SignupTwoFormState(
    val schoolError: Int? = null,
    val certificateError: Int? = null,
    val photoError: Int? = null,
    val isDataValid: Boolean = false
)