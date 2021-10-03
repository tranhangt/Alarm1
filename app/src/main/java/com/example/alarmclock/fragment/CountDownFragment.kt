package com.example.alarmclock.fragment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.example.alarmclock.databinding.FragmentCountDownBinding
import com.example.alarmclock.viewmodel.CountDownViewModel

class CountDownFragment : Fragment() {
    private lateinit var binding: FragmentCountDownBinding
    lateinit var cdViewModel: CountDownViewModel
    private var isActive = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCountDownBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.timepickercd.setIs24HourView(true)
        binding.timepickercd.hour = 0
        binding.timepickercd.minute = 1

        cdViewModel = ViewModelProvider(requireActivity()).get(CountDownViewModel::class.java)
        cdViewModel.timeLeft.observe(viewLifecycleOwner, {
            binding.txtTime.text = it
        })
        cdViewModel.timeOut.observe(viewLifecycleOwner, {
            if(it){
                resetTime()
                isActive = false
            }
        })
        binding.btnStart.setOnClickListener {
            val hour = binding.timepickercd.hour
            val min = binding.timepickercd.minute
            if(hour != 0 || min != 0) {
                isActive = true
                binding.btnStart.isEnabled = false
                binding.btnPause.isEnabled = true
                binding.btnStop.isEnabled = true
                binding.timepickercd.visibility = View.GONE
                binding.timeLeftLayout.visibility = View.VISIBLE

                cdViewModel.setTimeProgress(0, min, hour).toInt()
                cdViewModel.setNewTime(0, min, hour)
                cdViewModel.startTime()
            }
            else{
                Toast.makeText(context, "Bạn chưa chọn thời gian", Toast.LENGTH_LONG).show()
            }
        }
        binding.btnPause.setOnClickListener {
            if(isActive){
                cdViewModel.finishTime()
                binding.btnPause.text = "Resume"
                isActive = false
            }
            else{
                cdViewModel.startTime()
                isActive = true
                binding.btnPause.text = "Pause"
            }
        }
        binding.btnStop.setOnClickListener {
            isActive = false
            resetTime()
            cdViewModel.finishTime()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun resetTime(){
        binding.timepickercd.hour = 0
        binding.timepickercd.minute = 1
        binding.btnPause.isEnabled = false
        binding.btnStop.isEnabled = false
        binding.btnStart.isEnabled = true
        binding.btnPause.text = "Pause"
        binding.timepickercd.visibility = View.VISIBLE
        binding.timeLeftLayout.visibility = View.GONE
    }
}