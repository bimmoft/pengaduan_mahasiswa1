package com.example.pengaduan_mahasiswa

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class PengaduanActivity : AppCompatActivity() {
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_CAMERA_PERMISSION = 2
    private lateinit var currentPhotoPath: String
    private lateinit var database: DatabaseReference
    private lateinit var storage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main1)

        // Inisialisasi Firebase
        database = FirebaseDatabase.getInstance().getReference("Pengaduan")
        storage = FirebaseStorage.getInstance()

        setupAutoCompleteTextView()
        setupBackButton()
        setupCameraButton()
    }

    private fun setupAutoCompleteTextView() {
        val jenisPengaduan = arrayOf("Akademik", "Sarana dan Prasarana", "Pembelajaran", "Keamanan")
        val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, jenisPengaduan)
        autoCompleteTextView.setAdapter(adapter)
        autoCompleteTextView.setKeyListener(null) // Mencegah input manual
        autoCompleteTextView.setOnClickListener {
            autoCompleteTextView.showDropDown()
        }
    }

    private fun setupBackButton() {
        val backButton: ImageView = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }
    }

    private fun setupCameraButton() {
        val buttonOpenCamera: ImageView = findViewById(R.id.ButtonOpenCamera)
        buttonOpenCamera.setOnClickListener {
            if (checkCameraPermission() && checkStoragePermission()) {
                dispatchTakePictureIntent()
            } else {
                requestPermissions()
            }
        }
    }

    private fun checkCameraPermission(): Boolean {
        return checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkStoragePermission(): Boolean {
        return checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        requestPermissions(
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_CAMERA_PERMISSION
        )
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = createImageFile()
                photoFile?.let {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "${packageName}.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    private fun createImageFile(): File? {
        return try {
            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
            val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            if (storageDir != null) {
                File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
                    currentPhotoPath = absolutePath
                    Log.d("PengaduanActivity", "Image file created at: $currentPhotoPath") // Debug log
                }
            } else {
                Log.e("PengaduanActivity", "Storage directory is null")
                null
            }
        } catch (ex: IOException) {
            Log.e("PengaduanActivity", "Error creating image file", ex)
            null
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent()
            } else {
                Toast.makeText(this, "Izin kamera diperlukan untuk mengambil gambar.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageView: ImageView = findViewById(R.id.imageView12)
            val bitmap = BitmapFactory.decodeFile(currentPhotoPath)
            imageView.setImageBitmap(bitmap)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP

            // Unggah foto ke Firebase Storage
            val photoFile = File(currentPhotoPath)
            val fileUri = Uri.fromFile(photoFile)
            val fileName = photoFile.name
            val storageRef = storage.reference.child("pengaduan_images/$fileName")

            storageRef.putFile(fileUri)
                .addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        // Simpan data ke Firebase Realtime Database
                        val jenisPengaduan = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView).text.toString()
                        val pengaduanId = database.push().key ?: ""
                        val pengaduanData = mapOf(
                            "id" to pengaduanId,
                            "jenis" to jenisPengaduan,
                            "imageUrl" to uri.toString(),
                            "timestamp" to System.currentTimeMillis()
                        )
                        database.child(pengaduanId).setValue(pengaduanData)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(this, "Pengaduan berhasil disimpan!", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(this, "Gagal menyimpan pengaduan.", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Gagal mengunggah foto.", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Gambar tidak berhasil diambil.", Toast.LENGTH_SHORT).show()
        }
    }
}
