package com.example.mimedicokotlinformedics.services

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class MedicService {

    private val tag = "MedicService"

    suspend fun getMedic(medicId: String): DocumentSnapshot? {
        Log.d(tag,"Fetching a Medic...")
        return try {
            val doc = FirebaseFirestore.getInstance().collection("medics")
                .document(medicId)
                .get()
                .await()
            Log.d(tag,"Medic Fetched correctly")
            doc
        }catch (ex: Exception){
            Log.e(tag,ex.message!!)
            null
        }
    }

    suspend fun createMedic(medicId: String,
                           firstname: String,
                           lastname: String,
                           email: String,
                           curp: String,
                           school: String,
                           certUrl: String,
                           photoUrl: String,
                            business: String,
                            years: Int): Boolean{
        return try{
            Log.d(tag,"Creating a new medic...")
            FirebaseFirestore.getInstance().collection("medics")
                .document(medicId)
                .set(hashMapOf(
                    "firstname" to firstname,
                    "lastname" to lastname,
                    "email" to email,
                    "curp" to curp,
                    "school" to school,
                    "photoUrl" to photoUrl,
                    "certUrl" to certUrl,
                    "likes" to 0,
                    "timestamp" to Timestamp.now().seconds,
                    "business" to business,
                    "years" to years
                ))
                .await()
            Log.d(tag,"New medic created successfully")
            true
        }catch (ex : Exception){
            Log.e(tag,ex.message!!)
            false
        }

    }
}