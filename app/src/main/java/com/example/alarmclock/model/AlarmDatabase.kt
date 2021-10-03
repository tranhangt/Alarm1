package com.example.alarmclock.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Alarm::class], version = 1)
abstract class AlarmDatabase : RoomDatabase() {
    abstract fun Alarmdao(): AlarmDAO
    companion object{
        private var INSTANCE: AlarmDatabase? = null
        fun getDatabase(context: Context): AlarmDatabase{
            return INSTANCE?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AlarmDatabase::class.java,
                    "alarm_database").build()
                INSTANCE = instance
                instance
            }
        }
    }
}