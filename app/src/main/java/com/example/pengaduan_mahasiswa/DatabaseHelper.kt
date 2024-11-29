package com.example.pengaduan_mahasiswa

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "pengaduan_db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "pengaduan"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                ${Pengaduan.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${Pengaduan.COLUMN_JENIS_PENGADUAN} TEXT,
                ${Pengaduan.COLUMN_FOTO_URI} TEXT
            )
        """
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertPengaduan(pengaduan: Pengaduan): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(Pengaduan.COLUMN_JENIS_PENGADUAN, pengaduan.jenisPengaduan)
            put(Pengaduan.COLUMN_FOTO_URI, pengaduan.fotoUri)
        }
        val result = db.insert(TABLE_NAME, null, values)
        return result != -1L
    }
}
