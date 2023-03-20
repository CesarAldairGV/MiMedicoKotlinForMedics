package com.example.mimedicokotlinformedics.ui.signup

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(): ViewModel(){
    private val _signupForm = MutableLiveData<SignupFormState>()
    val signupForm : LiveData<SignupFormState> = _signupForm

    private fun checkFirstname(firstname: String): Boolean{
        return firstname.isNotEmpty()
    }

    private fun checkLastname(lastname: String): Boolean{
        return lastname.isNotEmpty()
    }

    private fun checkEmail(email: String): Boolean{
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun checkPhone(phone: String): Boolean{
        return phone.length == 10
    }

    private fun checkPassword(password: String): Boolean{
        return password.length > 5
    }

    fun checkData(firstname: String, lastname: String, email: String, phone: String, password: String){
        var firstnameError : Int? = null
        var lastnameError : Int? = null
        var emailError : Int? = null
        var phoneError : Int? = null
        var passwordError : Int? = null
        var isDataValid = false
        if(!checkFirstname(firstname)){
            firstnameError = 1
        }
        if(!checkLastname(lastname)){
            lastnameError = 1
        }
        if (!checkEmail(email)){
            emailError = 1
        }
        if (!checkPhone(phone)){
            phoneError = 1
        }
        if (!checkPassword(password)){
            passwordError = 1
        }
        if(firstnameError == null && lastnameError == null &&
            emailError == null && phoneError == null &&
            passwordError == null){
            isDataValid = true
        }
        _signupForm.value = SignupFormState(firstnameError, lastnameError, emailError, phoneError, passwordError, isDataValid)
    }
}