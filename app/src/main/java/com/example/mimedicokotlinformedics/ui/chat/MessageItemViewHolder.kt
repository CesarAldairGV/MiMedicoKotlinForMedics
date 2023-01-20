package com.example.mimedicokotlinformedics.ui.chat

import com.example.mimedicokotlinformedics.R
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class MessageItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val body = itemView.findViewById<TextView>(R.id.item_msg_body)
    private val photo = itemView.findViewById<ImageView>(R.id.item_msg_photo)
    private val date = itemView.findViewById<TextView>(R.id.item_msg_date)
    private val image = itemView.findViewById<ImageView>(R.id.item_msg_img)


    fun bindData(messageItem: MessageItem){
        date.text = messageItem.date
        if(messageItem.message == null){
            image.visibility = View.VISIBLE
            Picasso.get().load(messageItem.imgUrl).into(image)
            body.visibility = View.GONE
        }else{
            body.text = messageItem.message
            image.visibility = View.GONE
        }
        if(messageItem.photoUrl != null){
            Picasso.get().load(messageItem.photoUrl).into(photo)
        }
    }

}