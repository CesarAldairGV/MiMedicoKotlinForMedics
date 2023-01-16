package com.example.mimedicokotlinformedics.hilt

import android.app.Application
import com.example.mimedicokotlinformedics.services.AuthService
import com.example.mimedicokotlinformedics.services.MedicService
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {
    @Inject
    lateinit var authService: AuthService

    fun getCurrentMedicId(): String?{
        return authService.getCurrentMedic()?.uid
    }

    fun logout(){
        return authService.logout()
    }
}