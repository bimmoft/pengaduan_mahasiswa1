package com.example.pengaduan_mahasiswa;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainRegister extends AppCompatActivity {

    private DatabaseReference database; // Referensi ke Firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrasi); // Layout registrasi

        // Inisialisasi referensi Firebase
        database = FirebaseDatabase.getInstance().getReference("Users");

        // Ambil referensi ke elemen UI
        EditText nameInput = findViewById(R.id.nameEditText);
        EditText nimInput = findViewById(R.id.nimEditText);
        EditText passwordInput = findViewById(R.id.passwordEditText);
        Button registerButton = findViewById(R.id.registerButton);
        ImageView backButton = findViewById(R.id.back_button); // Tombol kembali (ImageView)

        // Aksi tombol registrasi
        registerButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String nim = nimInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (!name.isEmpty() && !nim.isEmpty() && !password.isEmpty()) {
                // Cek apakah NIM sudah terdaftar
                database.orderByChild("nim").equalTo(nim).get()
                        .addOnSuccessListener(dataSnapshot -> {
                            if (dataSnapshot.exists()) {
                                Toast.makeText(MainRegister.this, "NIM sudah terdaftar.", Toast.LENGTH_SHORT).show();
                            } else {
                                String userId = database.push().getKey();
                                if (userId != null) {
                                    User user = new User(name, nim, password);
                                    database.child(userId).setValue(user)
                                            .addOnCompleteListener(task -> {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(MainRegister.this, "Registrasi berhasil!", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                } else {
                                                    Toast.makeText(MainRegister.this, "Registrasi gagal, coba lagi.", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(MainRegister.this, "Gagal menyimpan data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            });
                                }
                            }
                        })
                        .addOnFailureListener(e -> {
                            // Tambahkan log untuk melihat error
                            Toast.makeText(MainRegister.this, "Kesalahan koneksi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });

            } else {
                Toast.makeText(MainRegister.this, "Harap isi semua field.", Toast.LENGTH_SHORT).show();
            }
        });

        // Aksi tombol kembali
        backButton.setOnClickListener(v -> {
            // Kembali ke halaman login
            finish();  // Menutup aktivitas registrasi dan kembali ke halaman login
        });
    }

    // Kelas User untuk menyimpan data pengguna
    public static class User {
        public String name;
        public String nim;
        public String password;

        // Constructor default (diperlukan oleh Firebase)
        public User() {}

        public User(String name, String nim, String password) {
            this.name = name;
            this.nim = nim;
            this.password = password;
        }
    }
}
