package com.codepath.bitfit2

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SleepEntryDao {
    @Query("SELECT * FROM sleep_entry_table")
    fun getAllSleepEntries(): Flow<List<SleepEntryEntity>>

    @Insert
    fun insert(entry: SleepEntryEntity)

    @Query("DELETE FROM sleep_entry_table")
    fun deleteAllSleepEntries()

    @Query("SELECT AVG(feelingRating) FROM sleep_entry_table")
    fun getFeelingAverage(): Double

    @Query("SELECT AVG(sleptHours) FROM sleep_entry_table")
    fun getHoursAverage(): Double

    @Query("SELECT sleptHours, feelingRating, sleepDate FROM sleep_entry_table")
    fun getChartEntries(): Flow<List<ChartEntryEntity>>


}