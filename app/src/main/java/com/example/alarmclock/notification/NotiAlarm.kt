package com.example.alarmclock.notification

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class NotiAlarm : Application() {
    companion object{
        const val CHANNEL_ID = "ALARM CHANNEL"
        const val CHANNEL_ID2 = "COUNTDOWN CHANNEL"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel(){
        //chi doi voi API 26+N
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(CHANNEL_ID, "channel alarm", NotificationManager.IMPORTANCE_DEFAULT)
            val channel2 = NotificationChannel(CHANNEL_ID2, "channel countdown", NotificationManager.IMPORTANCE_DEFAULT)
            val notificationManager:NotificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
            notificationManager.createNotificationChannel(channel2)
        }
    }
}