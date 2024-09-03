package com.example.hevintechnowebpractical.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hevintechnowebpractical.R
import com.example.hevintechnowebpractical.data.model.User
import com.example.hevintechnowebpractical.databinding.ItemUserBinding

class UserAdapter(private val users: MutableList<User>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.binding.apply {
            firstName.text = user.firstName
            lastName.text = user.lastName
            email.text = user.email

            // Load the image using Glide
            Glide.with(userImage.context)
                .load(user.image)
                .placeholder(R.drawable.ic_launcher_background) // Placeholder image resource
                .error(R.drawable.ic_launcher_foreground) // Error image resource
                .into(userImage)
        }
    }

    override fun getItemCount(): Int = users.size

    fun addUsers(newUsers: List<User>) {
        val start = users.size
        users.addAll(newUsers)
        notifyItemRangeChanged(start, newUsers.size)
    }
    fun clearAdapter(){
        users.clear()
        notifyDataSetChanged()
    }
}

