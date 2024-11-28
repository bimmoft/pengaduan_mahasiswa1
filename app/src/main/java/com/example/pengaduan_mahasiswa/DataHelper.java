package com.example.pengaduan_mahasiswa;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "PengaduanMahasiswa.db"; // Nama Database
    private static final int DATABASE_VERSION = 1; // Versi Database

    // Nama Tabel dan Kolom
    public static final String TABLE_NAME = "pengaduan";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_JUDUL = "judul";
    public static final String COLUMN_SOLUSI = "solusi";
    public static final String COLUMN_JENIS = "jenis";
    public static final String COLUMN_DESKRIPSI = "deskripsi";
    public static final String COLUMN_BUKTI = "bukti"; // Path file foto

    // SQL untuk Membuat Tabel
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_JUDUL + " TEXT, " +
            COLUMN_SOLUSI + " TEXT, " +
            COLUMN_JENIS + " TEXT, " +
            COLUMN_DESKRIPSI + " TEXT, " +
            COLUMN_BUKTI + " TEXT);";

    public DataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Membuat Tabel
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Menghapus tabel lama jika versi database diperbarui
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
