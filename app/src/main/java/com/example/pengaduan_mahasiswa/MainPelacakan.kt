package com.example.pengaduan_mahasiswa

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class PelacakanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pelacakan)

        // Ambil referensi ke back button dan linear layout
        val backButton: ImageView = findViewById(R.id.back_button)
        val linearLayoutPelacakan: LinearLayout = findViewById(R.id.linearLayoutPelacakan)
        val linearLayoutPelacakan1: LinearLayout = findViewById(R.id.linearLayoutPelacakan1)

        // Aksi saat linearLayoutPelacakan diklik
        linearLayoutPelacakan.setOnClickListener {
            val intent = Intent(this, FormActivity::class.java)
            startActivity(intent)
        }

        // Aksi saat linearLayoutPelacakan1 diklik
        linearLayoutPelacakan1.setOnClickListener {
            // Misalnya membuka activity lain atau aksi tertentu
            val intent = Intent(this, FormActivity1::class.java)
            startActivity(intent)
        }

        // Aksi saat backButton diklik (kembali ke halaman sebelumnya)
        backButton.setOnClickListener {
            finish()
        }
    }
}
