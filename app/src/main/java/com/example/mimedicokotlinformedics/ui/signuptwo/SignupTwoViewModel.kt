package com.example.mimedicokotlinformedics.ui.signuptwo

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mimedicokotlinfirebase.dto.MedicSignupRequest
import com.example.mimedicokotlinfirebase.services.MedicAuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupTwoViewModel @Inject constructor(
    private val authService: MedicAuthService
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
               photo: Bitmap,
               business: String,
               years: Int){
        viewModelScope.launch {
            val req = MedicSignupRequest(
                firstName = firstname,
                lastName = lastname,
                email = email,
                phone = curp,
                password = password,
                school = school,
                business = business,
                yearsExp = years
            )
            _signupResult.value =
                authService.signup(req,cert,photo)
        }
    }

    private fun checkBusiness(business: String?): Boolean{
        return business != null
    }
    private fun checkYears(years: Int?): Boolean{
        return (years != null) && (years >= 1)
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

    fun checkData(school: String,cert: Bitmap?,photo: Bitmap?,business: String?, years: Int?){
        var certError: Int? = null
        var photoError: Int? = null
        var schoolError: Int? = null
        var businessError: Int? = null
        var yearsError: Int? = null
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
        if(!checkBusiness(business)){
            businessError = 1
        }
        if(!checkYears(years)){
            yearsError = 1
        }
        if(certError == null && photoError == null && schoolError == null
            && yearsError == null && businessError == null){
            isDataValid = true
        }
        _signupFormState.value = SignupTwoFormState(schoolError,
            certError,photoError,businessError,yearsError,isDataValid)
    }
}