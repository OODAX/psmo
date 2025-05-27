package com.example.mvvmdbapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(
    private var users: List<User>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    interface OnItemClickListener {
        fun onItemUpdate(user: User)
        fun onItemDelete(user: User)
    }

    fun updateData(newUsers: List<User>) {
        users = newUsers
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_2, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.tvName.text = user.name
        holder.tvEmail.text = user.email

        holder.itemView.setOnClickListener {
            listener.onItemUpdate(user)
        }

        holder.itemView.setOnLongClickListener {
            listener.onItemDelete(user)
            true
        }
    }

    override fun getItemCount() = users.size

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(android.R.id.text1)
        val tvEmail: TextView = itemView.findViewById(android.R.id.text2)
    }
}
// This code defines a UserAdapter for a RecyclerView in an Android application.
// The adapter binds a list of User objects to the RecyclerView, allowing for display and interaction with user data.
// It includes an interface for item click listeners to handle updates and deletions of users.
// The updateData method allows the adapter to refresh its data and notify the RecyclerView of changes.
// The onCreateViewHolder method inflates the layout for each item, while onBindViewHolder sets the data for each item.
// The UserViewHolder class holds references to the TextViews for displaying user information.
// The adapter is part of a Room database implementation in an Android application, enabling efficient data management and UI updates.
