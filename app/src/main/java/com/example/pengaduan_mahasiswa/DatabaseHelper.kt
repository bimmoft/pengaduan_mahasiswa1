package com.example.pengaduan_mahasiswa

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "pengaduan.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "pengaduan"
        private const val COLUMN_ID = "id"
        private const val COLUMN_JENIS = "jenis"
        private const val COLUMN_IMAGE_PATH = "image_path"
        private const val COLUMN_TIMESTAMP = "timestamp"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID TEXT PRIMARY KEY," +
                "$COLUMN_JENIS TEXT," +
                "$COLUMN_IMAGE_PATH TEXT," +
                "$COLUMN_TIMESTAMP INTEGER)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertPengaduan(id: String, jenis: String, imagePath: String, timestamp: Long): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID, id)
            put(COLUMN_JENIS, jenis)
            put(COLUMN_IMAGE_PATH, imagePath)
            put(COLUMN_TIMESTAMP, timestamp)
        }
        val result = db.insert(TABLE_NAME, null, values)
        db.close()
        return result != -1L
    }
}

