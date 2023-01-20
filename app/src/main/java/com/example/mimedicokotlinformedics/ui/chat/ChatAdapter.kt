package com.example.mimedicokotlinformedics.ui.chat

import com.example.mimedicokotlinformedics.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges

class ChatAdapter(val recyclerView: RecyclerView, options: FirestoreRecyclerOptions<MessageItem>):
    FirestoreRecyclerAdapter<MessageItem, MessageItemViewHolder>(options){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageItemViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message, parent, false)
        return MessageItemViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MessageItemViewHolder,
        position: Int,
        model: MessageItem
    ) {
        holder.bindData(model)
    }

    override fun onDataChanged() {
        super.onDataChanged()
        recyclerView.scrollToPosition(this.itemCount - 1)
    }

    companion object {
        fun getAdapter(consultId: String, recyclerView: RecyclerView): ChatAdapter {

            val query = FirebaseFirestore.getInstance().collection("consults")
                .document(consultId)
                .collection("chat")
                .orderBy("timestamp")
                .limitToLast(7)

            val options = FirestoreRecyclerOptions.Builder<MessageItem>()
                .setQuery(query,
                    MetadataChanges.EXCLUDE){
                    it.toMessageItem()
                }
                .build()

            return ChatAdapter(recyclerView, options)
        }

        private fun DocumentSnapshot.toMessageItem(): MessageItem =
            MessageItem(
                message = this["message", String::class.java],
                photoUrl = this["photoUrl", String::class.java],
                imgUrl = this["imgUrl", String::class.java],
                date = this["date", String::class.java]!!
            )
    }

}