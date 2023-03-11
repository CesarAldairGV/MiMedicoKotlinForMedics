package com.example.mimedicokotlinformedics.services

import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ConsultService {

    private val tag = "ConsultService"

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    suspend fun createConsult(
        proposalId: String,
        petitionId: String,
        medicId: String,
        userId: String,
        medicName: String,
        userName: String,
        subject: String,
        date: String
    ): Boolean {
        return try{
            Log.d(tag, "Creating a consult...")
            FirebaseFirestore.getInstance().collection("consults")
                .document()
                .set(
                    hashMapOf(
                        "petitionId" to petitionId,
                        "proposalId" to proposalId,
                        "medicId" to medicId,
                        "userId" to userId,
                        "subject" to subject,
                        "medicName" to medicName,
                        "userName" to userName,
                        "timestamp" to Timestamp.now().seconds,
                        "date" to date
                    )
                )
                .await()
            Log.d(tag, "Consult created successfully")
            true
        }catch(ex: Exception){
            Log.e(tag,ex.message!!)
            false
        }
    }

    suspend fun sendMessage(consultId: String, message: String, photoUrl: String): Boolean{
        return try{
            Log.d(tag, "Sending a message...")
            FirebaseFirestore.getInstance().collection("consults")
                .document(consultId)
                .collection("chat")
                .add(hashMapOf(
                    "message" to message,
                    "date" to LocalDateTime.now().format(formatter),
                    "timestamp" to Timestamp.now().seconds,
                    "photoUrl" to photoUrl
                ))
                .await()
            Log.d(tag, "Message Sent successfully...")
            true
        }catch (ex : Exception){
            Log.e(tag, ex.message!!)
            false
        }
    }

    suspend fun sendImage(consultId: String, bitmap: Bitmap, photoUrl: String): Boolean{
        return try{
            Log.d(tag, "Sending a Image...")
            val bytes = getImageBytes(bitmap)
            val res = FirebaseStorage.getInstance()
                .getReference("chatimg")
                .child(UUID.randomUUID().toString())
                .putBytes(bytes)
                .await()
            val url = res.storage.downloadUrl.await()
            FirebaseFirestore.getInstance().collection("consults")
                .document(consultId)
                .collection("chat")
                .add(hashMapOf(
                    "imgUrl" to url,
                    "date" to LocalDateTime.now().format(formatter),
                    "timestamp" to Timestamp.now().seconds,
                    "photoUrl" to photoUrl
                ))
                .await()
            Log.d(tag, "Image Sent Successfully")
            true
        }catch (ex: Exception){
            Log.e(tag, ex.message!!)
            false
        }
    }

    private fun getImageBytes(bitmap: Bitmap): ByteArray {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        return baos.toByteArray()
    }

    suspend fun getConsultData(consultId: String): DocumentSnapshot?{
        return FirebaseFirestore.getInstance().collection("consults")
            .document(consultId)
            .get()
            .await()
    }
}