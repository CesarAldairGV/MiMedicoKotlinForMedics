package com.example.mimedicokotlinformedics.ui.petitions

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.mimedicokotlinformedics.R
import com.squareup.picasso.Picasso

class PetitionItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val subject: TextView = itemView.findViewById(R.id.item_pet_subj)
    private val date: TextView = itemView.findViewById(R.id.item_pet_date)
    private val body: TextView = itemView.findViewById(R.id.item_pet_body)
    private val img: ImageView = itemView.findViewById(R.id.item_pet_img)
    private val proposalButton: TextView = itemView.findViewById(R.id.item_pet_act)

    fun bindData(petitionItem: PetitionItem){
        subject.text = petitionItem.subject
        date.text = petitionItem.date
        body.text = petitionItem.body
        if(petitionItem.urlPhoto != null) {
            img.visibility = View.VISIBLE
            Picasso.get().load(petitionItem.urlPhoto).into(img)
        }
        else img.visibility = ImageView.GONE

        proposalButton.setOnClickListener{
            val bundle = bundleOf("petitionId" to petitionItem.petitionId)
//            itemView.findNavController().navigate(R.id.action_PetitionsFragment_to_ProposalsFragment, bundle)
        }
    }
}