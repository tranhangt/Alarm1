package com.example.alarmclock.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.alarmclock.model.Alarm
import com.example.alarmclock.model.AlarmDatabase
import com.example.alarmclock.model.AlarmRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmViewModel(app : Application): AndroidViewModel(app){
    private val alarmDAO = AlarmDatabase.getDatabase(app).Alarmdao()
    private val alarmRepository = AlarmRepository(alarmDAO)
    var listAlarm: LiveData<List<Alarm>> = alarmRepository.data

    fun insert(alarm: Alarm){
        viewModelScope.launch(Dispatchers.IO){
            alarmRepository.insert(alarm)
        }
        listAlarm = alarmRepository.data
    }

    fun update(alarm: Alarm){
        viewModelScope.launch(Dispatchers.IO){
            alarmRepository.update(alarm)
        }
    }

    fun delete(alarm: Alarm){
        viewModelScope.launch(Dispatchers.IO){
            alarmRepository.delete(alarm)
        }
    }

}