package com.example.pengaduan_mahasiswa

data class Pengaduan(
    val jenisPengaduan: String,
    val fotoUri: String
) {
    companion object {
        const val COLUMN_ID = "id"
        const val COLUMN_JENIS_PENGADUAN = "jenis_pengaduan"
        const val COLUMN_FOTO_URI = "foto_uri"
    }
}
