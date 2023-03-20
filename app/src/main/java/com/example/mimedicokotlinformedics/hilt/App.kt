package com.example.mimedicokotlinformedics.hilt

import android.app.Application
import com.example.mimedicokotlinfirebase.services.MedicAuthService
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {
    @Inject
    lateinit var authService: MedicAuthService

    fun getCurrentMedicId(): String?{
        return authService.getCurrentMedicId()
    }

    fun logout(){
        return authService.logout()
    }
}