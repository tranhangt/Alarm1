package com.example.alarmclock.model

import androidx.lifecycle.LiveData

class AlarmRepository(private var alarmDAO: AlarmDAO) {
    var data: LiveData<List<Alarm>> = alarmDAO.getAll()
    suspend fun insert(alarm: Alarm){
        alarmDAO.insert(alarm)
    }
    suspend fun update(alarm: Alarm){
        alarmDAO.update(alarm)
    }
    suspend fun delete(alarm: Alarm){
        alarmDAO.delete(alarm)
    }
}