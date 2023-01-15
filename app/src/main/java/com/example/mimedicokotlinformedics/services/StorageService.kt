package com.example.mimedicokotlinformedics.services

import android.graphics.Bitmap
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.UUID

class StorageService {

    suspend fun uploadImg(path: String, img: Bitmap): String{
        val bytes = getImageBytes(img)
        val storage = FirebaseStorage.getInstance()
        val imgId = UUID.randomUUID().toString()
        val uri = storage.getReference("$path/$imgId")
            .putBytes(bytes)
            .await()
            .storage
            .downloadUrl
            .await()
        return uri.toString()
    }

    private fun getImageBytes(bitmap: Bitmap): ByteArray {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        return baos.toByteArray()
    }
}