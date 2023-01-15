package com.example.mimedicokotlinformedics.hilt

import com.example.mimedicokotlinformedics.services.AuthService
import com.example.mimedicokotlinformedics.services.MedicService
import com.example.mimedicokotlinformedics.services.StorageService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun providesMedicService() = MedicService()

    @Provides
    fun providesStorageService() = StorageService()

    @Provides
    fun providesAuthService() = AuthService(providesMedicService(), providesStorageService())
}