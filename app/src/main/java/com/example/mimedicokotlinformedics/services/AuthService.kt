package com.example.mimedicokotlinformedics.services

import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.tasks.await

class AuthService(
    private val medicService: MedicService,
    private val storageService: StorageService
) {
    private val tag = "AuthService"

    suspend fun login(email: String, password: String): Int {
        val auth = FirebaseAuth.getInstance()
        try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            if (!result.user!!.isEmailVerified){
                Log.d(tag, "Medic not verified")
                auth.signOut()
                return 1
            }
            if(!medicService.getMedic(result.user!!.uid)!!.exists()) {
                auth.signOut()
                return 2
            }
            Log.d(tag, "Login Successfully")
            return 0
        } catch (ex: Exception) {
            Log.e(tag,ex.message!!)
            return 2
        }
    }

    suspend fun signup(firstname: String,
                       lastname: String,
                       email: String,
                       curp: String,
                       password: String,
                       school: String,
                       cert: Bitmap,
                       photo: Bitmap): Boolean {
        return try {

            val firebaseAuth = FirebaseAuth.getInstance()

            val signupResult = firebaseAuth
                .createUserWithEmailAndPassword(email, password).await()

            firebaseAuth.signInWithEmailAndPassword(email,password)

            val certUrl = storageService.uploadImg("medicFaces",cert)
            val photoUrl = storageService.uploadImg("medicCerts",photo)

            val userId = signupResult.user!!.uid
            signupResult.user!!.sendEmailVerification().await()
            medicService.createMedic(userId, firstname, lastname, email, curp,school,certUrl, photoUrl)
            Log.d(tag,"Signup Successfully")

            firebaseAuth.signOut()
            true
        }catch (ex : Exception){
            Log.d(tag,ex.message!!)
            false
        }
    }

    fun getCurrentMedic(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    suspend fun getCurrentMedicInfo(): DocumentSnapshot?{
        val medicId = getCurrentMedic()?.uid
        if(medicId != null) return medicService.getMedic(medicId)
        return null
    }

    fun logout(){
        FirebaseAuth.getInstance().signOut()
    }
}