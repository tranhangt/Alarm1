package com.example.alarmclock.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AlarmDAO {
    @Insert
    suspend fun insert(alarm: Alarm)

    @Query("SELECT * from alarm_table")
    fun getAll(): LiveData<List<Alarm>>

    @Update
    suspend fun update(alarm: Alarm)

    @Delete
    suspend fun delete(alarm: Alarm)
}