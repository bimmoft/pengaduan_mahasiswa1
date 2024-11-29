package com.example.pengaduan_mahasiswa

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class AdminActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private lateinit var database: DatabaseReference
    private lateinit var usersList: MutableList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        usersList = mutableListOf()

        // Initialize the adapter with delete and logout actions
        userAdapter = UserAdapter(usersList, { user -> deleteUser(user) }, { logout() })
        recyclerView.adapter = userAdapter

        // Reference to Firebase node Users
        database = FirebaseDatabase.getInstance().getReference("Users")

        // Fetch data from Firebase
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Clear the list before adding new data
                usersList.clear()

                // Add users to the list (ensuring no duplicates based on NIM)
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(User::class.java)
                    user?.let {
                        // Prevent duplicates by checking if the user already exists
                        if (!usersList.any { it.nim == user.nim }) {
                            usersList.add(user)
                        }
                    }
                }
                userAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Failed to load users", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteUser(user: User) {
        database.orderByChild("nim").equalTo(user.nim).get()
            .addOnSuccessListener { snapshot ->
                for (childSnapshot in snapshot.children) {
                    childSnapshot.ref.removeValue()
                }
                // Update the list after deletion
                usersList.remove(user)
                userAdapter.notifyDataSetChanged()
                Toast.makeText(applicationContext, "User deleted successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext, "Failed to delete user", Toast.LENGTH_SHORT).show()
            }
    }

    private fun logout() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish() // Close AdminActivity
    }
}
