package com.example.facebookclone.Fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.facebookclone.DataClasses.User
import com.example.facebookclone.R

class OnlineUserAdapter(private val onClick: (User) -> Unit) : RecyclerView.Adapter<OnlineUserAdapter.ViewHolder>() {

    private var users: List<User> = listOf()

    fun updateUsers(users: List<User>) {
        this.users = users
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_online_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user)
        holder.itemView.setOnClickListener {
            onClick(user)
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userName: TextView = itemView.findViewById(R.id.textViewUserName)
        private val userStatus: TextView = itemView.findViewById(R.id.textViewOnlineStatus)
        private val userImage: ImageView = itemView.findViewById(R.id.imageViewUserProfile)

        fun bind(user: User) {
            // Concatenate first name and last name to form the full name
            userName.text = "${user.firstName} ${user.lastName}"

            // Use onlineStatus instead of isOnline
            userStatus.text = if (user.onlineStatus) "Online" else "Offline"

            // Load the user image if available
            Glide.with(itemView.context).load(user.profileImageUrl).into(userImage)
        }
    }
}




