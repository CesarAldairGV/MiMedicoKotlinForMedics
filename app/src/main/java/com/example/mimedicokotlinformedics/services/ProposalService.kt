package com.example.mimedicokotlinformedics.services

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ProposalService(
    private val authService: AuthService
) {
    private val tag = "ProposalService"
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    suspend fun sendProposal(petitionId: String, message : String){
        var medic = authService.getCurrentMedicInfo()

        FirebaseFirestore.getInstance()
            .collection("proposals")
            .add(hashMapOf(
                "body" to message,
                "school" to medic!!["school"],
                "date" to LocalDateTime.now().format(formatter),
                "timestamp" to Timestamp.now().seconds,
                "medicName" to "${medic!!["firstname"]} ${medic!!["lastname"]}",
                "likes" to medic!!["likes"],
                "medicId" to medic.id,
                "petitioniId" to petitionId,
                "photoUrl" to medic!!["photoUrl"],

                //Fix This
                "bussiness" to "IMSS",
                "yearsExp" to 10
            ))
            .await()
    }

}