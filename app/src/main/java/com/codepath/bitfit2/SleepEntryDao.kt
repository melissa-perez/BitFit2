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

    @Query("SELECT ROUND(AVG(feelingRating), 2) FROM sleep_entry_table")
    fun getFeelingAverage(): Double

    @Query("SELECT ROUND(AVG(sleptHours), 2) FROM sleep_entry_table")
    fun getHoursAverage(): Double

    @Query("SELECT ROUND(MAX(feelingRating), 2) FROM sleep_entry_table")
    fun getMaxFeeling(): Double

    @Query("SELECT ROUND(MIN(feelingRating), 2) FROM sleep_entry_table")
    fun getMinFeeling(): Double

    @Query("SELECT ROUND(MAX(sleptHours), 2) FROM sleep_entry_table")
    fun getMaxSleep(): Double

    @Query("SELECT ROUND(MIN(sleptHours), 2) FROM sleep_entry_table")
    fun getMinSleep(): Double

    @Query("SELECT sleptHours, feelingRating, sleepDate FROM sleep_entry_table")
    fun getChartEntries(): Flow<List<ChartEntryEntity>>


}