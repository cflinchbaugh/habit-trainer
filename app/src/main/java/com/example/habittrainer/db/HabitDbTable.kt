package com.example.habittrainer.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.habittrainer.CreateHabitActivity
import com.example.habittrainer.Habit
import com.example.habittrainer.db.HabitEntry.COL_DESCRIPTION
import com.example.habittrainer.db.HabitEntry.COL_ID
import com.example.habittrainer.db.HabitEntry.COL_IMAGE
import com.example.habittrainer.db.HabitEntry.COL_TITLE
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
            COL_ID,
            COL_TITLE,
            COL_DESCRIPTION,
            COL_IMAGE
        )
        val orderBy = "${HabitEntry.COL_ID} ASC"
        val cursor = db.doQuery(
            HabitEntry.TABLE_NAME,
            columns,
            orderBy = orderBy
        )
        val habits = mutableListOf<Habit>()

        while(cursor.moveToNext()) {
            val title = cursor.getStringFromColumn(HabitEntry.COL_TITLE)
            val description = cursor.getStringFromColumn(HabitEntry.COL_DESCRIPTION)
            val bitmap = cursor.getBitmapFromColumn(HabitEntry.COL_IMAGE)

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

    private fun SQLiteDatabase.doQuery(
            table: String,
            columns: Array<String>,
            selection: String? = null,
            selectionsArgs: Array<String>? = null,
            groupBy: String? = null,
            having: String? = null,
            orderBy: String? = null
        ): Cursor {
        return query(table, columns, selection, selectionsArgs, groupBy, having, orderBy)
    }

    private fun Cursor.getStringFromColumn(columnName: String): String {
        return this.getString(this.getColumnIndexOrThrow(columnName))
    }

    private fun Cursor.getBitmapFromColumn(columnName: String): Bitmap {
        val byteArray = this.getBlob(this.getColumnIndexOrThrow(columnName))
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
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