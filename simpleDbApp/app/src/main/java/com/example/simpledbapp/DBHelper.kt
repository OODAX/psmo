package com.example.simpledbapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, "SimpleDB", null, 2) { // zmiana wersji na 2

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE Items(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, email TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Items")
        onCreate(db)
    }

    fun insertItem(name: String, email: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", name)
            put("email", email)
        }
        db.insert("Items", null, values)
    }

    fun getAllItems(): List<Triple<Int, String, String>> {
        val db = readableDatabase
        val list = mutableListOf<Triple<Int, String, String>>()
        val cursor = db.rawQuery("SELECT * FROM Items", null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val name = cursor.getString(1)
            val email = cursor.getString(2)
            list.add(Triple(id, name, email))
        }
        cursor.close()
        return list
    }

    fun updateItem(id: Int, name: String, email: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", name)
            put("email", email)
        }
        db.update("Items", values, "id=?", arrayOf(id.toString()))
    }

    fun deleteItem(id: Int) {
        val db = writableDatabase
        db.delete("Items", "id=?", arrayOf(id.toString()))
    }
}
