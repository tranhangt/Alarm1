package com.example.alarmclock.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.alarmclock.R
import com.example.alarmclock.databinding.FragmentSetTimeBinding
import com.example.alarmclock.model.Alarm
import com.example.alarmclock.viewmodel.AlarmViewModel

class SetTimeFragment : Fragment() {
    private lateinit var binding : FragmentSetTimeBinding
    private val viewModel: AlarmViewModel by viewModels<AlarmViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSetTimeBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.txtSave.setOnClickListener {
            createNewAlarm()
            Navigation.findNavController(view).navigate(R.id.action_setTimeFragment_to_homeFragment)
        }
        binding.txtBack.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_setTimeFragment_to_homeFragment)
        }
        return view
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun createNewAlarm(){
        Log.e("create alarm", "new")
        var newAlarm = Alarm(
            binding.timepicker.hour,
            binding.timepicker.minute,
            binding.checkMon.isChecked,
            binding.checkTue.isChecked,
            binding.checkWed.isChecked,
            binding.checkThu.isChecked,
            binding.checkFri.isChecked,
            binding.checkSat.isChecked,
            binding.checkSun.isChecked,
            binding.edtReminder.text.toString(),
            true,
            System.currentTimeMillis()
        )
        viewModel.insert(newAlarm)
        val hour = String.format("%02d",binding.timepicker.hour)
        val minute = String.format("%02d", binding.timepicker.minute)
        Toast.makeText(requireContext(), "$hour : $minute", Toast.LENGTH_LONG).show()
        activity?.let {
            newAlarm.makeSchedule(it as Context)
        }
    }

}