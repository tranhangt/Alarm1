package com.example.alarmclock.service

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.alarmclock.R
import com.example.alarmclock.broadcastReceiver.NotiReceiver
import com.example.alarmclock.notification.NotiAlarm.Companion.CHANNEL_ID2

class CountDownService: Service(){
    private lateinit var mediaPlayer: MediaPlayer

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.e("on count down service", "hello")
        mediaPlayer = MediaPlayer.create(this, R.raw.music)
        mediaPlayer.isLooping = true
        intent?.let {
            val key = it.getIntExtra("notiID", 0)
            if(key == 3){
                stopSelf()
            }
        }
        sendNotification()
        mediaPlayer.start()
        return START_STICKY
    }

    private fun sendNotification(){
        val notiIntent = Intent(this, NotiReceiver::class.java)
        notiIntent.putExtra("notiID", 3)
        val actionIntent = PendingIntent.getBroadcast(this, 0, notiIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID2)
            .setContentText("Đã hết giờ")
            .setContentTitle("Đếm giờ")
            .setSmallIcon(R.drawable.ic_timer)
            .addAction(R.drawable.ic_timer, "Bỏ qua", actionIntent)
            .setAutoCancel(true)
            .build()
        startForeground(1, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
    }
}