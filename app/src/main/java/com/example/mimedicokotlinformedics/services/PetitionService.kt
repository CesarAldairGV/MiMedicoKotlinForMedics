package com.example.mimedicokotlinformedics.services

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class PetitionService {

    private val tag = "PetitionService"

    suspend fun getPetitionById(petitionId: String): DocumentSnapshot? {
        return try{
            Log.d(tag, "Fetching a petition...")
            val doc = FirebaseFirestore.getInstance().collection("petitions")
                .document(petitionId)
                .get()
                .await()
            Log.d(tag, "Petition fetched")
            doc
        }catch (ex: Exception){
            Log.d(tag,ex.message!!)
            null
        }
    }
}