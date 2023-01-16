package com.example.mimedicokotlinformedics.ui.petitions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mimedicokotlinformedics.R
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges

class PetitionsAdapter(options: FirestoreRecyclerOptions<PetitionItem>):
    FirestoreRecyclerAdapter<PetitionItem, PetitionItemViewHolder>(options){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetitionItemViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_petition, parent, false)
        return PetitionItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: PetitionItemViewHolder, position: Int, model: PetitionItem) {
        holder.bindData(model)
    }

    companion object{

        fun getAdapter(): PetitionsAdapter{

            val query = FirebaseFirestore.getInstance().collection("petitions")
                .whereEqualTo("finished",false)
                .orderBy("timestamp")

            val options = FirestoreRecyclerOptions.Builder<PetitionItem>()
                .setQuery(query, MetadataChanges.EXCLUDE) {
                    it.toPetitionItem()
                }
                .build()

            return PetitionsAdapter(options)
        }

        private fun DocumentSnapshot.toPetitionItem(): PetitionItem{
            return PetitionItem(
                petitionId = this.id,
                subject = this["subject", String::class.java]!!,
                date = this["date", String::class.java]!!,
                body = this["body", String::class.java]!!,
                urlPhoto = this["urlPhoto", String::class.java]
            )
        }
    }

}