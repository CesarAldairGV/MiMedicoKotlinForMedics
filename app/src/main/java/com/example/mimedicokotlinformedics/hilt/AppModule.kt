package com.example.mimedicokotlinformedics.hilt

import com.example.mimedicokotlinfirebase.services.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun providesUserService(): UserService = UserService()

    @Provides
    fun providesMedicService(): MedicService = MedicService()

    @Provides
    fun providesStorageService(): StorageService = StorageService()

    @Provides
    fun providesMedicAuthService(): MedicAuthService = MedicAuthService(providesMedicService(),providesStorageService())

    @Provides
    fun providesPetitionService(): PetitionService =
        PetitionService(providesUserService(), providesStorageService())

    @Provides
    fun providesConsultService(): ConsultService = ConsultService(providesStorageService())

    @Provides
    fun providesProposalService(): ProposalService =
        ProposalService(providesMedicService(),providesConsultService(),providesPetitionService())

    @Provides
    fun providesCommentService(): CommentService =
        CommentService(providesUserService(), providesConsultService())
}