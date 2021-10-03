package com.example.alarmclock.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.alarmclock.service.AlarmService
import com.example.alarmclock.service.CountDownService

class NotiReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            val id = intent.getIntExtra("notiID", 0)
            if(id == 1){
                val intentSer = Intent(context, AlarmService::class.java)
                intentSer.putExtra("notiID", id)
                context?.startService(intentSer)
            }
            if(id == 3){
                val intentSer = Intent(context,CountDownService::class.java)
                intentSer.putExtra("notiID", id)
                context?.startService(intentSer)
            }
        }
    }

}