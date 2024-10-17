package com.example.pengaduan_mahasiswa

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ambil referensi ke tombol-tombol
        val profileButton: ImageView = findViewById(R.id.profile_image)
        val pengaduanButton: ImageView = findViewById(R.id.pengaduan_button)
        val pelacakanButton: ImageView = findViewById(R.id.pelacakan_button)
        val notifikasiButton: ImageView = findViewById(R.id.notifikasi_button)

        // Atur aksi saat notifikasiButton diklik
        notifikasiButton.setOnClickListener {
            val intent = Intent(this, NotifikasiActivity::class.java)
            startActivity(intent)
        }

        // Atur aksi saat pelacakanButton diklik
        pelacakanButton.setOnClickListener {
            val intent = Intent(this, PelacakanActivity::class.java)
            startActivity(intent)
        }

        // Atur aksi saat pengaduanButton diklik
        pengaduanButton.setOnClickListener {
            val intent = Intent(this, PengaduanActivity::class.java)
            startActivity(intent)
        }

        // Atur aksi saat profileButton diklik
        profileButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
}
