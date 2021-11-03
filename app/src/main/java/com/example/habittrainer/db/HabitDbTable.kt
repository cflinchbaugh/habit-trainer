package com.example.habittrainer.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.habittrainer.CreateHabitActivity
import com.example.habittrainer.Habit
import java.io.ByteArrayOutputStream

class HabitDbTable(context: Context) {
    private val dbHelper = HabitTrainerDb(context)
    private val simpleNameTag = CreateHabitActivity::class.simpleName

    fun store(habit: Habit): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues()
        with(values) {
            put(HabitEntry.COL_TITLE, habit.title)
            put(HabitEntry.COL_DESCRIPTION, habit.description)
            put(HabitEntry.COL_IMAGE, toByteArray(habit.image))
        }

        val id = db.transaction {
            it.insert(HabitEntry.TABLE_NAME, null, values)
        }

        Log.d(simpleNameTag, "Stored new habit in the db: $habit")

        return id
    }

    fun readAllHabits(): List<Habit> {
        val db = dbHelper.readableDatabase
        val columns = arrayOf(
            HabitEntry.COL_ID,
            HabitEntry.COL_TITLE,
            HabitEntry.COL_DESCRIPTION,
            HabitEntry.COL_IMAGE
        )
        val orderBy = "${HabitEntry.COL_ID} ASC"
        val cursor = db.query(
            HabitEntry.TABLE_NAME,
            columns,
            null,
            null,
            null,
            null,
            orderBy
        )
        val habits = mutableListOf<Habit>()

        while(cursor.moveToNext()) {
            val title = cursor.getString(cursor.getColumnIndexOrThrow(HabitEntry.COL_TITLE))
            val description = cursor.getString(cursor.getColumnIndexOrThrow(HabitEntry.COL_DESCRIPTION))
            val byteArray = cursor.getBlob(cursor.getColumnIndexOrThrow(HabitEntry.COL_IMAGE))
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

            habits.add(Habit(title, description, bitmap))
        }

        cursor.close()

        return habits
    }

    private fun toByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream)
        return stream.toByteArray()
    }

}

// Inline because this takes in an anon function to avoid extra object creations
private inline fun <T> SQLiteDatabase.transaction(function: (SQLiteDatabase) -> T): T {
    beginTransaction()
    val result = try {
        val returnValue = function(this)
        setTransactionSuccessful()

        returnValue
    } finally {
        endTransaction()
    }

    close()

    return result
}