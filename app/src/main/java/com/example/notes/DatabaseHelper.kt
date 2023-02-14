package com.example.notes

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.notes.Constants.DATABASE_NAME
import com.example.notes.Constants.DATABASE_VERSION
import com.example.notes.Constants.ENTITY_NOTE
import com.example.notes.Constants.PROPERTY_DESCRIPTION
import com.example.notes.Constants.PROPERTY_ID
import com.example.notes.Constants.PROPERTY_IS_FINISHED

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME,
    null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $ENTITY_NOTE (" +
                "$PROPERTY_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$PROPERTY_DESCRIPTION VARCHAR(60), " +
                "$PROPERTY_IS_FINISHED BOOLEAN)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    @SuppressLint("Range")
    fun getAllNotes(): MutableList<Note> {
        val notes: MutableList<Note> = mutableListOf()

        val database = this.readableDatabase
        val query = "SELECT * FROM $ENTITY_NOTE"

        val result = database.rawQuery(query, null)

        if (result.moveToFirst()) {
            do {
                val note = Note()
                note.id = result.getLong(result.getColumnIndex(PROPERTY_ID))
                note.description = result.getString(
                    result.getColumnIndex(PROPERTY_DESCRIPTION))
                note.isFinished = result.getInt(
                    result.getColumnIndex(PROPERTY_IS_FINISHED)) == Constants.TRUE

                notes.add(note)
            } while (result.moveToNext())
        }

        return notes
    }

    fun insertNote(note: Note): Long {
        val database = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(PROPERTY_DESCRIPTION, note.description)
            put(PROPERTY_IS_FINISHED, note.isFinished)
        }

        val resultId = database.insert(ENTITY_NOTE,
            null,
            contentValues)

        return resultId
    }

    fun updateNote(note: Note): Boolean {
        val database = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(PROPERTY_DESCRIPTION, note.description)
            put(PROPERTY_IS_FINISHED, note.isFinished)
        }

        val result = database.update(ENTITY_NOTE, contentValues,
            "$PROPERTY_ID = ${note.id}",  //"id = 1"
            null)

        return result == Constants.TRUE
    }

    fun deleteNote(note: Note): Boolean {
        val database = this.writableDatabase
        val result = database.delete(ENTITY_NOTE, "$PROPERTY_ID = ${note.id}", null)

        return result == Constants.TRUE
    }
}