package com.example.pengaduan_mahasiswa

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2) // Ganti dengan layout yang sesuai untuk Login

        // Ambil referensi ke tombol "Masuk"
        val loginButton: Button = findViewById(R.id.loginButton)

        // Aksi ketika tombol "Masuk" ditekan
        loginButton.setOnClickListener {
            // Membuat intent untuk berpindah ke MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) // Menjalankan MainActivity
            finish() // Menutup LoginActivity agar tidak bisa kembali dengan tombol back
        }
    }
}
