package com.example.githubuserapi.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.githubuserapi.db.DatabaseGithub.FavColums.Companion.AVATAR
import com.example.githubuserapi.db.DatabaseGithub.FavColums.Companion.LOGIN
import com.example.githubuserapi.db.DatabaseGithub.FavColums.Companion.TABLE_NAME
import com.example.githubuserapi.db.DatabaseGithub.FavColums.Companion._ID

internal class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "dbgithubuserapi"
        private const val DATABASE_VERSION = 1
        private const val DATABASE_TABLE = "CREATE TABLE $TABLE_NAME" +
                " ($_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " $LOGIN TEXT NOT NULL UNIQUE," +
                " $AVATAR TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(DATABASE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}