package com.example.pengaduan_mahasiswa

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {

    // Referensi Firebase
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2) // Ganti dengan layout login Anda

        // Referensi ke Firebase Realtime Database
        database = FirebaseDatabase.getInstance().getReference("Users")

        // Ambil referensi ke elemen UI
        val nameInput: EditText = findViewById(R.id.editText1) // Nama
        val nimInput: EditText = findViewById(R.id.editText2) // NIM
        val passwordInput: EditText = findViewById(R.id.passwordEditText) // Password
        val loginButton: Button = findViewById(R.id.loginButton) // Tombol Login
        val registerButton: Button = findViewById(R.id.registerButton) // Tombol Registrasi

        // Aksi tombol login
        loginButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val nim = nimInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (name.isNotEmpty() && nim.isNotEmpty() && password.isNotEmpty()) {
                // Cek apakah username, password dan NIM sesuai dengan admin
                if (name == "admin" && password == "admin" && nim == "123") {
                    // Login Admin
                    Toast.makeText(this, "Login Admin", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, AdminActivity::class.java) // Ganti dengan activity admin Anda
                    startActivity(intent)
                    finish()
                } else {
                    // Cek apakah NIM sudah terdaftar di Firebase
                    database.orderByChild("nim").equalTo(nim).get()
                        .addOnSuccessListener { dataSnapshot ->
                            if (dataSnapshot.exists()) {
                                // NIM ditemukan, cek password
                                val user = dataSnapshot.children.firstOrNull()
                                if (user != null) {
                                    val storedPassword = user.child("password").getValue(String::class.java)

                                    if (storedPassword == password) {
                                        // Password cocok, lanjutkan ke MainActivity
                                        Toast.makeText(this, "Login berhasil!", Toast.LENGTH_SHORT).show()
                                        val intent = Intent(this, MainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        // Password salah
                                        Toast.makeText(this, "Password salah.", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    // Data pengguna tidak ditemukan meskipun NIM ditemukan
                                    Toast.makeText(this, "Terjadi kesalahan, coba lagi.", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                // NIM tidak ditemukan, arahkan ke registrasi
                                Toast.makeText(this, "NIM tidak terdaftar, silakan registrasi.", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, MainRegister::class.java)
                                startActivity(intent)
                            }
                        }.addOnFailureListener {
                            // Jika terjadi kesalahan saat query ke Firebase
                            Toast.makeText(this, "Terjadi kesalahan, coba lagi.", Toast.LENGTH_SHORT).show()
                        }
                }
            } else {
                Toast.makeText(this, "Harap isi semua field.", Toast.LENGTH_SHORT).show()
            }
        }

        // Aksi tombol registrasi
        registerButton.setOnClickListener {
            // Berpindah ke halaman registrasi
            val intent = Intent(this, MainRegister::class.java)
            startActivity(intent)
        }
    }
}
