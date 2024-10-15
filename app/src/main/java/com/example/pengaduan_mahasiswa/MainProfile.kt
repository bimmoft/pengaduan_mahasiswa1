package com.example.pengaduan_mahasiswa

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val backButton: ImageView = findViewById(R.id.back_button)
        val keluarButton: Button = findViewById(R.id.button_keluar)

        // Atur aksi saat back_button dipencet
        backButton.setOnClickListener {
            finish()
        }

        // Atur aksi saat keluar_button dipencet
        keluarButton.setOnClickListener {
            // Kembali ke MainLoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Menutup ProfileActivity
        }
    }
}