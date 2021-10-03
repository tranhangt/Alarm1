package com.example.alarmclock.service

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.os.Vibrator
import androidx.core.app.NotificationCompat
import com.example.alarmclock.AlarmRing
import com.example.alarmclock.R
import com.example.alarmclock.broadcastReceiver.AlarmReceiver
import com.example.alarmclock.broadcastReceiver.NotiReceiver
import com.example.alarmclock.notification.NotiAlarm.Companion.CHANNEL_ID

class AlarmService: Service() {
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var vibrator: Vibrator

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        //tao nhac chuong bao thuc
        mediaPlayer = MediaPlayer.create(this, R.raw.music)
        mediaPlayer.isLooping = true
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            val key = it.getIntExtra("notiID", 0)
            if(key == 1){
                stopSelf()
            }
        }
        val skipIntent = Intent(this, NotiReceiver::class.java)  //truyen du lieu sang class NotiReceiver
        skipIntent.putExtra("notiID", 1)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, skipIntent, 0)

        //tao 1 thong bao ben ngoai ung dung
        val nofitication = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Chuông báo!!")
            .setContentText(intent?.getStringExtra("REMINDER"))
            .setSmallIcon(R.drawable.clock)
            .setContentIntent(pendingIntent) //kich hoat intent khi cham vao thong bao
            .addAction(R.drawable.clock, "Bỏ qua", pendingIntent)
            .setAutoCancel(true)
            .build()

        mediaPlayer.start()
        //bật thông báo
        startForeground(1, nofitication)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        vibrator.cancel()
    }
}