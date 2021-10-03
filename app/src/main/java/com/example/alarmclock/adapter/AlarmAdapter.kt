package com.example.alarmclock.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.alarmclock.R
import com.example.alarmclock.databinding.AlarmItemBinding
import com.example.alarmclock.model.Alarm

class AlarmAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {

    var alarmList = emptyList<Alarm>()

    inner class AlarmViewHolder(val binding: AlarmItemBinding): RecyclerView.ViewHolder(binding.root),
        View.OnClickListener{
        fun bind(alarm: Alarm){
            Log.e("bind", "check")
            binding.itemSwitch.setChecked(alarm.isOn)
            binding.itemHour.text = String.format("%02d", alarm.hour)
            binding.itemMinute.text = String.format("%02d", alarm.minute)
            binding.itemRecurring.text = alarm.getRecurring()
            if(alarm.reminder.length > 10){
                binding.itemReminder.text = alarm.reminder.subSequence(0, 10)
            }
            else{
                binding.itemReminder.text = alarm.reminder
            }
            Log.e("done bind", "yep")
        }
        init {
            binding.itemSwitch.setOnClickListener(this)
            binding.itemHour.setOnClickListener {
                listener.onViewClick(adapterPosition)
            }
            binding.itemMinute.setOnClickListener {
                listener.onViewClick(adapterPosition)
            }
            Log.e("init", "e")
        }
        override fun onClick(v: View?) {
            val position = adapterPosition
            if(position != RecyclerView.NO_POSITION){
                listener.onItemClick(position, binding.itemSwitch.isChecked)
            }
        }
        @SuppressLint("ResourceAsColor")
        fun turnSwitch(onSwitch: Boolean){
            if(!onSwitch){
                binding.itemTwoDot.setTextColor(R.color.grey)
                binding.itemHour.setTextColor(R.color.grey)
                binding.itemMinute.setTextColor(R.color.grey)
                binding.itemRecurring.setTextColor(R.color.grey)
                binding.itemReminder.setTextColor(R.color.grey)
            }
        }
    }
    interface OnItemClickListener{
        fun onItemClick(position: Int, onSwitch: Boolean)
        fun onViewClick(position: Int)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        return AlarmViewHolder(AlarmItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val alarm = alarmList[position]
        holder.bind(alarm)
    }

    override fun getItemCount(): Int {
        return alarmList.size
    }

    fun setData(alarmData: List<Alarm>){
        Log.e("set data", "stuck")
        val alarmDiffUtil = AlarmDiffUtil(alarmList, alarmData)
        val alarmDiffResult =DiffUtil.calculateDiff(alarmDiffUtil)
        this.alarmList = alarmData
        alarmDiffResult.dispatchUpdatesTo(this)
    }
}