package com.monzo.androidtest.room

import androidx.room.TypeConverter
import java.util.Date

class TypeConverters {
    @TypeConverter
    fun fromDate(date: Date): Long = date.time

    @TypeConverter
    fun toDate(millisSinceEpoch: Long): Date = millisSinceEpoch.let { Date(it) }
}