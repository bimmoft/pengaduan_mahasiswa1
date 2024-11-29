package com.example.pengaduan_mahasiswa

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class PengaduanActivity : AppCompatActivity() {
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_CAMERA_PERMISSION = 2
    private lateinit var currentPhotoPath: String
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var buttonKirim: Button
    private lateinit var imageView: ImageView
    private lateinit var autoCompleteTextView: AutoCompleteTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main1)

        // Inisialisasi SQLite
        databaseHelper = DatabaseHelper(this)

        // Inisialisasi komponen UI
        buttonKirim = findViewById(R.id.button3)
        imageView = findViewById(R.id.imageView12)
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView)

        // Setup UI
        setupAutoCompleteTextView()
        setupBackButton()
        setupCameraButton()

        // Tombol "Kirim" disabled awal
        buttonKirim.isEnabled = false

        buttonKirim.setOnClickListener {
            handleSendData()
        }
    }

    private fun setupAutoCompleteTextView() {
        val jenisPengaduan = arrayOf("Akademik", "Sarana dan Prasarana", "Pembelajaran", "Keamanan")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, jenisPengaduan)
        autoCompleteTextView.setAdapter(adapter)
        autoCompleteTextView.setKeyListener(null)
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
            if (checkPermissions()) {
                dispatchTakePictureIntent()
            } else {
                requestPermissions()
            }
        }
    }

    private fun checkPermissions(): Boolean {
        val cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val storagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        return cameraPermission == PackageManager.PERMISSION_GRANTED &&
                storagePermission == PackageManager.PERMISSION_GRANTED
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
                    val photoURI = FileProvider.getUriForFile(
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
            File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
                currentPhotoPath = absolutePath
                Log.d("PengaduanActivity", "Image file created at: $currentPhotoPath")
            }
        } catch (ex: IOException) {
            Log.e("PengaduanActivity", "Error creating image file", ex)
            null
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                dispatchTakePictureIntent()
            } else {
                Toast.makeText(this, "Izin diperlukan untuk mengambil gambar.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val bitmap = BitmapFactory.decodeFile(currentPhotoPath)
            imageView.setImageBitmap(bitmap)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            buttonKirim.isEnabled = true // Aktifkan tombol "Kirim" setelah foto diambil
        } else {
            Toast.makeText(this, "Gambar tidak berhasil diambil.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleSendData() {
        val jenisPengaduan = autoCompleteTextView.text.toString()
        val pengaduanId = UUID.randomUUID().toString()
        val timestamp = System.currentTimeMillis()

        if (jenisPengaduan.isEmpty() || currentPhotoPath.isEmpty()) {
            Toast.makeText(this, "Isi semua data sebelum mengirim.", Toast.LENGTH_SHORT).show()
            return
        }

        val success = databaseHelper.insertPengaduan(
            pengaduanId,
            jenisPengaduan,
            currentPhotoPath,
            timestamp
        )
        if (success) {
            Toast.makeText(this, "Pengaduan berhasil disimpan.", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Gagal menyimpan pengaduan.", Toast.LENGTH_SHORT).show()
        }
    }
}