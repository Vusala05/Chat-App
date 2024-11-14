package com.example.chatapp.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.Model.User
import com.example.chatapp.R
import com.example.chatapp.View.ChatActivity
import com.example.chatapp.databinding.UserBinding
import com.squareup.picasso.Picasso

class UserAdapter(private val userList: ArrayList<User>, private val isChat : Boolean):RecyclerView.Adapter<UserAdapter.UserHolder>() {
    class UserHolder( val binding: UserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        val binding = UserBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return UserHolder(binding)
    }

    override fun getItemCount(): Int {
       return userList.size
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        if(isChat){
            if(userList[position].status==("online")){
                holder.binding.statusOn.visibility = View.VISIBLE
                holder.binding.statusOff.visibility = View.GONE
            }
            else{
                holder.binding.statusOn.visibility = View.GONE
                holder.binding.statusOff.visibility = View.VISIBLE
            }
        }else{
            holder.binding.statusOn.visibility = View.GONE
            holder.binding.statusOff.visibility = View.GONE
        }

        holder.binding.nameInRow.text = userList[position].name
        Picasso.get().load(userList[position].url).error(R.drawable.ic_launcher_background).into(holder.binding.image);
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ChatActivity::class.java)
            intent.putExtra("name",userList[position].name)
            intent.putExtra("uid",userList[position].uuid)


            holder.itemView.context.startActivity(intent)
        }
    }
}