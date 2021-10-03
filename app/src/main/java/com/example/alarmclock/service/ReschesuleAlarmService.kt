package com.example.alarmclock.service

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import com.example.alarmclock.model.Alarm
import com.example.alarmclock.model.AlarmDatabase
import com.example.alarmclock.model.AlarmRepository

class ReschesuleAlarmService: Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        var alarmDao = AlarmDatabase.getDatabase(applicationContext).Alarmdao()
        var alarmRepository = AlarmRepository(alarmDao)
        alarmRepository.data.value.let {
            if(it != null){
                for(alarm: Alarm in it){
                    if(alarm.isOn){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            alarm.makeSchedule(applicationContext)
                        }
                    }
                }
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}