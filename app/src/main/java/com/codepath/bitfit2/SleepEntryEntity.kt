package com.codepath.bitfit2

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.Date

@Entity(tableName = "sleep_entry_table")

data class SleepEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "sleptHours") val sleptHours: Float,
    @ColumnInfo(name = "feelingRating") val feelingRating: Int,
    @ColumnInfo(name = "sleepNotes") val sleepNotes: String?,
    @ColumnInfo(name = "sleepDate") val sleepDate: String?,
)