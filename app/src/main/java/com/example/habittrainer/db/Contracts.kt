package com.example.habittrainer.db

import android.provider.BaseColumns

const val DATABASE_NAME = "habittrainer.db"
const val DATABASE_VERSION = 10

object HabitEntry : BaseColumns {
    const val COL_DESCRIPTION = "description"
    const val COL_ID = "id"
    const val COL_IMAGE = "image"
    const val COL_TITLE = "title"
    const val TABLE_NAME = "habit"

}

