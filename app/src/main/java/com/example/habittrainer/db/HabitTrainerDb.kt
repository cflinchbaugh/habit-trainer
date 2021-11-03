package com.example.habittrainer.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class HabitTrainerDb(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    private val sqlCreateEntries = """
        CREATE TABLE ${HabitEntry.TABLE_NAME} (
            ${HabitEntry.COL_ID} INTEGER PRIMARY KEY,
            ${HabitEntry.COL_TITLE} TEXT,
            ${HabitEntry.COL_DESCRIPTION} TEXT,
            ${HabitEntry.COL_IMAGE} BLOB
        )"""

    private val sqlDeleteEntries = "DROP TABLE IF EXISTS ${HabitEntry.TABLE_NAME}"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(sqlCreateEntries)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL(sqlDeleteEntries)
        onCreate(db)
    }

}