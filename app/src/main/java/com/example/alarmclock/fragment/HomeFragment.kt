package com.example.alarmclock.fragment

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.alarmclock.R
import com.example.alarmclock.adapter.AlarmAdapter
import com.example.alarmclock.databinding.FragmentHomeBinding
import com.example.alarmclock.model.Alarm
import com.example.alarmclock.viewmodel.AlarmViewModel
import java.util.*

class HomeFragment : Fragment(), AlarmAdapter.OnItemClickListener {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: AlarmViewModel by viewModels<AlarmViewModel>()
    private val alarmAdapter: AlarmAdapter by lazy { AlarmAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        val view = binding.root
        binding.imgAdd.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_setTimeFragment)
        }

        setUpRecyclerView()
        Log.e("set up rcv", "not done")
        viewModel.listAlarm.observe(viewLifecycleOwner, { data ->
            alarmAdapter.setData(data)
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setUpRecyclerView(){
        Log.e("recyclerView", "Hello")
        val rcv = binding.rcvAlarm
        rcv.adapter = alarmAdapter
        rcv.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        swipeToDelete(rcv)
    }
    private fun swipeToDelete(recyclerView: RecyclerView){
        val swipeLeftCallback = object: SwipeLeft(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deleteItem = alarmAdapter.alarmList[viewHolder.adapterPosition]
                val builder = AlertDialog.Builder(context)
                builder.setPositiveButton("Có"){_, _ ->
                    Toast.makeText(context, "Xóa thành công", Toast.LENGTH_LONG).show()
                    viewModel.delete(deleteItem)
                    alarmAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                }
                builder.setNegativeButton("Không"){_, _ ->
                    alarmAdapter.notifyDataSetChanged()
                }
                builder.setTitle("Xóa báo thức?")
                builder.create().show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeLeftCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
    //bat hay tat bao thuc
    override fun onItemClick(position: Int, onSwitch: Boolean) {
        val alarmData = alarmAdapter.alarmList[position]
        alarmData.isOn = onSwitch
        Log.e("onSwitch", onSwitch.toString())
        viewModel.update(alarmData)
    }

    override fun onViewClick(position: Int) {
        var alarmData = alarmAdapter.alarmList[position]
        val cal = Calendar.getInstance()
        val time = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
            cal.set(Calendar.MINUTE, minute)
            alarmData.minute = cal.get(Calendar.MINUTE)
            alarmData.hour = cal.get(Calendar.HOUR_OF_DAY)
            viewModel.update(alarmData)
            alarmAdapter.notifyDataSetChanged()
            Toast.makeText(context, "Cập nhật thành công!", Toast.LENGTH_SHORT).show()
        }
        TimePickerDialog(context, time, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }
}