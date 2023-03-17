package com.example.mimedicokotlinformedics.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mimedicokotlinfirebase.dto.Medic
import com.example.mimedicokotlinfirebase.services.MedicAuthService
import com.example.mimedicokotlinfirebase.services.MedicService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val medicService : MedicService,
    private val authService: MedicAuthService
): ViewModel() {

    private val _profileData: MutableLiveData<ProfileData> = MutableLiveData()
    val profileData: LiveData<ProfileData> get() = _profileData


    fun loadProfileData(){
        viewModelScope.launch {
            _profileData.value = medicService.getMedic(authService.getCurrentMedicId()!!)?.toProfileData()
        }
    }

    private fun Medic.toProfileData(): ProfileData =
        ProfileData(
            name = "${this.firstName} ${this.lastName}",
            email = this.email,
            curp = this.phone,
            school = this.school,
            certUrl = this.certUrl,
            photoUrl = this.photoUrl,
        )

}