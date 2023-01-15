package com.example.mimedicokotlinformedics.ui.signuptwo

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mimedicokotlinformedics.services.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupTwoViewModel @Inject constructor(
    private val authService: AuthService
): ViewModel() {

    private val _signupFormState: MutableLiveData<SignupTwoFormState> = MutableLiveData()
    val signupFormState : LiveData<SignupTwoFormState> get() = _signupFormState

    private val _signupResult: MutableLiveData<Boolean> = MutableLiveData()
    val signupResult : LiveData<Boolean> get() = _signupResult

    fun signup(firstname: String,
               lastname: String,
               email: String,
               curp: String,
               password: String,
               school: String,
               cert: Bitmap,
               photo: Bitmap){
        viewModelScope.launch {
            _signupResult.value =
                authService.signup(firstname, lastname, email, curp, password, school, cert, photo)
        }
    }

    private fun checkCert(cert: Bitmap?): Boolean{
        return cert != null
    }

    private fun checkPhoto(photo: Bitmap?): Boolean{
        return photo != null
    }

    private fun checkSchool(school: String): Boolean{
        return !school.isNullOrBlank()
    }

    fun checkData(school: String,cert: Bitmap?,photo: Bitmap?){
        var certError: Int? = null
        var photoError: Int? = null
        var schoolError: Int? = null
        var isDataValid = false
        if(!checkSchool(school)){
            schoolError = 1
        }
        if(!checkCert(cert)){
            certError = 1
        }
        if(!checkPhoto(photo)){
            photoError = 1
        }
        if(certError == null && photoError == null && schoolError == null){
            isDataValid = true
        }
        _signupFormState.value = SignupTwoFormState(schoolError, certError,photoError,isDataValid)
    }
}