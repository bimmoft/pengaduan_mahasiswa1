package com.example.pengaduan_mahasiswa

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(
    private val users: List<User>,
    private val onDeleteClick: (User) -> Unit,
    private val onLogoutClick: () -> Unit // Add the third parameter for logout
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int = users.size

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.nameText)
        private val nimTextView: TextView = itemView.findViewById(R.id.nimText)
        private val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
        private val logoutButton: Button = itemView.findViewById(R.id.logoutButton)

        fun bind(user: User) {
            nameTextView.text = user.name
            nimTextView.text = user.nim

            deleteButton.setOnClickListener {
                onDeleteClick(user)
            }

            logoutButton.setOnClickListener {
                onLogoutClick() // Logout logic
            }
        }
    }
}
