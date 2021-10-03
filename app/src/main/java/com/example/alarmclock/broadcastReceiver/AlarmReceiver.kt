package com.example.alarmclock.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import com.example.alarmclock.service.AlarmService
import com.example.alarmclock.service.ReschesuleAlarmService
import java.util.*

class AlarmReceiver : BroadcastReceiver() {
    //khi den gio da dat, receiver chay
    override fun onReceive(context: Context, intent: Intent) {
        Log.e("in receiver", "hello")
        // dong thoi gui du lieu sang AlarmService, kich hoat Service
        if(Intent.ACTION_BOOT_COMPLETED == intent.action){
            Toast.makeText(context, "Alarm Reboot", Toast.LENGTH_LONG).show()
            startReschesuleAlarmService(context)
        }
        else{
            Toast.makeText(context, "Alarm Received", Toast.LENGTH_LONG).show()
            if(!intent.getBooleanExtra("MON", false) &&
                !intent.getBooleanExtra("TUE", false) &&
                !intent.getBooleanExtra("WED", false) &&
                !intent.getBooleanExtra("THU", false) &&
                !intent.getBooleanExtra("FRI", false) &&
                !intent.getBooleanExtra("SAT", false) &&
                !intent.getBooleanExtra("SUN", false)){
                startAlarmService(context, intent)
            }
            else if(isTodayHasAlarm(intent)){
                startAlarmService(context, intent)
            }
        }
    }

    private fun isTodayHasAlarm(intent: Intent):Boolean{
        var calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        var today:Int = calendar.get(Calendar.DAY_OF_WEEK)
        when(today){
            Calendar.MONDAY ->{
                if(intent.getBooleanExtra("MON", false)) return true
                return false
            }
            Calendar.TUESDAY ->{
                if(intent.getBooleanExtra("TUE", false)) return true
                return false
            }
            Calendar.WEDNESDAY ->{
                if(intent.getBooleanExtra("WED", false)) return true
                return false
            }
            Calendar.THURSDAY ->{
                if(intent.getBooleanExtra("THU", false)) return true
                return false
            }
            Calendar.FRIDAY ->{
                if(intent.getBooleanExtra("FRI", false)) return true
                return false
            }
            Calendar.SATURDAY ->{
                if(intent.getBooleanExtra("SAT", false)) return true
                return false
            }
            Calendar.SUNDAY ->{
                if(intent.getBooleanExtra("SUN", false)) return true
                return false
            }
        }
        return false
    }
    private fun startAlarmService(context: Context, intent: Intent){
        //tạo intentSer truyền dữ liệu sang Service
        var intentSer = Intent(context, AlarmService::class.java)
        intentSer.putExtra("HOUR", intent.getIntExtra("HOUR", 0))
        intentSer.putExtra("MINUTE", intent.getIntExtra("MINUTE", 0))
        intentSer.putExtra("REMINDER", intent.getStringExtra("REMINDER"))

        //đối với Android 8+ (API 26+)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            context.startForegroundService(intentSer)
        }
        else{
            context.startService(intentSer)
        }
    }

    private fun startReschesuleAlarmService(context:Context){
        var intentSer = Intent(context, ReschesuleAlarmService::class.java)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            context.startForegroundService(intentSer)
        }
        else{
            context.startService(intentSer)
        }
    }
}