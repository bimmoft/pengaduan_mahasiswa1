package com.example.pengaduan_mahasiswa

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class FormActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_pelacakan)

        // Menggunakan findViewById untuk mengambil referensi ke back_button
        val backButton: ImageView = findViewById(R.id.back_button)

        // Atur aksi saat back_button dipencet
        backButton.setOnClickListener {
            // Tambahkan animasi jika diinginkan
            finish()  // Menutup activity ini
        }
    }
}
