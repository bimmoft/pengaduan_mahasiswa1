package com.example.pengaduan_mahasiswa

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class FormActivity1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_pelacakan1)

        // Menggunakan findViewById untuk mengambil referensi ke back_button dan button_konfirmasi
        val backButton: ImageView = findViewById(R.id.back_button)
        val buttonKonfirmasi: Button = findViewById(R.id.button_konfirmasi)

        // Atur aksi saat back_button dipencet
        backButton.setOnClickListener {
            finish()  // Menutup activity ini
        }

        // Atur aksi saat button_konfirmasi dipencet
        buttonKonfirmasi.setOnClickListener {
            // Membuat intent untuk kembali ke PelacakanActivity
            val intent = Intent(this, PelacakanActivity::class.java)

            // Menambahkan flag untuk membersihkan stack dan menghindari kembali ke FormActivity1
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()  // Menutup FormActivity1 setelah berpindah ke PelacakanActivity
        }
    }
}
