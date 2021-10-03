package com.example.alarmclock.model

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.alarmclock.broadcastReceiver.AlarmReceiver
import java.util.*

@Entity(tableName = "alarm_table")
data class Alarm(
    var hour: Int,
    var minute: Int,
    var monday: Boolean,
    var tuesday: Boolean,
    var wednesday: Boolean,
    var thursday: Boolean,
    var friday: Boolean,
    var saturday: Boolean,
    var sunday: Boolean,
    var reminder: String,
    var isOn: Boolean, //bao thuc co dang bat khong?
    @PrimaryKey(autoGenerate = true) var id: Long = 0
) {
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun makeSchedule(context: Context){
        Log.e("makeSchedule", "newSchedule")
        //cho phep truy cap vao he thong bao dong cua may
        var alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        //tao 1 intend gui du lieu den receiver
        val intent = Intent(context, AlarmReceiver::class.java)
        Log.e("add intend", "yep")
        intent.putExtra("HOUR", hour)
        intent.putExtra("MINUTE", minute)
        intent.putExtra("MON", monday)
        intent.putExtra("TUE", tuesday)
        intent.putExtra("WED", wednesday)
        intent.putExtra("THU", thursday)
        intent.putExtra("FRI", friday)
        intent.putExtra("SAT", saturday)
        intent.putExtra("SUN", sunday)
        intent.putExtra("REMINDER", reminder)

        var pendingIntent= id.let{ PendingIntent.getBroadcast(context, it.toInt(), intent, 0) }

        //lay thoi gian hien tai
        var calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)

        if(!monday && !tuesday && !wednesday && !thursday && !friday && !saturday && !sunday){
            Log.e("Alarm", "!recurring")
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
        else{
            val daily: Long = 24*60*60*1000
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                daily,
                pendingIntent
            )
        }
        Log.e("id shedule", id.toString())
        Log.e("schedule pending intent", pendingIntent.toString())
        this.isOn = true
    }

    fun cancelAlarm(context: Context){
        var alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        var pendingIntent:PendingIntent = id.let { PendingIntent.getBroadcast(context, it.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT) }
        alarmManager.cancel(pendingIntent)
        this.isOn = false
    }

    fun getRecurring():String{
        if(!monday && !tuesday && !wednesday && !thursday && !friday && !saturday && !sunday){
            return ""
        }
        var recurring = ""
        if(monday)recurring += "2 "
        if(tuesday)recurring += "3 "
        if(wednesday)recurring += "4 "
        if(thursday)recurring += "5 "
        if(friday)recurring += "6 "
        if(saturday)recurring += "7 "
        if(sunday)recurring += "C "
        return recurring
    }
}