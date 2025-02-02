package com.example.chatapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.Model.Message
import com.example.chatapp.R
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context: Context,private val messageList: ArrayList<Message>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
      val ITEM_RECEIVE = 1
      val ITEM_SENT = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
         if(viewType == 1){
             //inflate receive
             val view : View = LayoutInflater.from(context).inflate(R.layout.receive,parent,false)
             return ReceiveViewHolder(view)

         }else{
             val view : View = LayoutInflater.from(context).inflate(R.layout.sent,parent,false)
             return SentViewHolder(view)
             //inflate sent
         }
    }


    override fun getItemViewType(position: Int): Int {

        val currentMessage = messageList[position]

        if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            return ITEM_SENT
        }else{
            return ITEM_RECEIVE
        }
    }

    override fun getItemCount(): Int {
        return messageList.size

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]

        if(holder.javaClass == SentViewHolder::class.java){
            //do the stuff for sendViewHolder

            val viewHolder = holder as SentViewHolder
            holder.sentMessage.text = currentMessage.message

        }else{
            //do the stuff for receiveViewHolder
            val viewHolder = holder as ReceiveViewHolder
            holder.receiveMessage.text = currentMessage.message

        }

    }
    class SentViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val sentMessage = itemView.findViewById<TextView>(R.id.send_message)

    }
    class ReceiveViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val receiveMessage = itemView.findViewById<TextView>(R.id.receive_message)

    }

}