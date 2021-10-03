package com.example.alarmclock.viewmodel

import android.app.Application
import android.content.Intent
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alarmclock.R
import com.example.alarmclock.service.CountDownService

class CountDownViewModel(application: Application): AndroidViewModel(application){
    private lateinit var countDownTimer: CountDownTimer
    var timeLeft = MutableLiveData<String>()
    var timeOut = MutableLiveData<Boolean>()
    var time = 0L
    var timeLength = 0L
    private lateinit var mediaPlayer: MediaPlayer
    private val context by lazy {
        application.applicationContext
    }
    fun startTime(){
        Log.e("count down", "start")
        timeOut.postValue(false)
        countDownTimer = object : CountDownTimer(time * 1000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                time = millisUntilFinished/1000
                updateUI()
            }
            override fun onFinish() {
                Log.e("complete", "count down")
                timeOut.postValue(true)
                createService()
//                mediaPlayer = MediaPlayer.create(context, R.raw.music)
//                mediaPlayer.start()
                finishTime()
            }
        }.start()
    }

    fun setTimeProgress(sec: Int, min: Int, hour: Int):Long{
        Log.e("Count", (hour * 3600 + min * 60 + sec).toString())
        return (hour * 3600 + min * 60 + sec).toLong()
    }
    fun setNewTime(sec: Int, min: Int, hour: Int){
        time = (hour * 3600 + min * 60 + sec).toLong()
        timeLength = time
    }
    private fun updateUI(){
        val hour = time/3600
        val min = time/60 % 60
        val sec = time % 60
        timeLeft.postValue(String.format("%02d", hour) + ":" + String.format("%02d", min) + ":" + String.format("%02d", sec))
    }
    fun finishTime(){
        countDownTimer.cancel()
    }
    private fun createService() {
        Log.e("create", "service cd")
        var intent = Intent(context, CountDownService::class.java)
        context.startService(intent)
    }
}