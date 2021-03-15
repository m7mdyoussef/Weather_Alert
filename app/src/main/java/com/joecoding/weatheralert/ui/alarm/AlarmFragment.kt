package com.joecoding.weatheralert.ui.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.joecoding.weatheralert.R
import com.joecoding.weatheralert.adapter.AlarmAdapter
import com.joecoding.weatheralert.databinding.FragmentAlarmBinding
import com.joecoding.weatheralert.model.currentWeatherModel.db.localSourceDB.pojo.alarmModel.AlarmModel
import com.joecoding.weatheralert.providers.SharedPreferencesProvider
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AlarmFragment : Fragment() {

    private var _binding: FragmentAlarmBinding? = null
    private val binding get() = _binding!!

    lateinit var sharedPref: SharedPreferencesProvider

    lateinit var alarmViewModel: AlarmViewModel
   private var alarmList = mutableListOf<AlarmModel>()
    private  lateinit var alarmAdapter: AlarmAdapter

    private var calenderEvent = Calendar.getInstance()

    private var repeating: Int =24

    private var alarmSwitchedOn: Boolean =false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {




        _binding = FragmentAlarmBinding.inflate(inflater, container, false)
        alarmViewModel = ViewModelProvider.AndroidViewModelFactory
            .getInstance(requireActivity().application)
            .create(AlarmViewModel::class.java)


        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref = SharedPreferencesProvider(requireContext())
        alarmSwitchedOn = sharedPref.isAlarmSwitchedOn
        fabAlarmClicked()
        updateAlarmRecycler()
        fetchAlarmItems()
        checkValidations()
        switchAlarm()
        forNextHoursCheck()
        timeInputCheckEvents()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkValidations(){
        if(alarmSwitchedOn==true){
            binding.imgAlarm.setBackgroundResource(R.drawable.alarm)
            binding.checkEventLayout.visibility = View.GONE
            binding.fornextradioGroup.visibility = View.GONE
            sharedPref.alarmSwitchedOn(true)
            binding.fabAlarm.visibility=View.GONE
           // registerAll()
            alarmViewModel.registerAll(context,alarmList,repeating,calenderEvent, activity as AppCompatActivity)
        }else{
            binding.imgAlarm.setBackgroundResource(R.drawable.alarm_off)
            binding.checkEventLayout.visibility = View.VISIBLE
            binding.fornextradioGroup.visibility = View.VISIBLE
            sharedPref.alarmSwitchedOn(false)
            binding.fabAlarm.visibility=View.VISIBLE
//            unRegisterAll()
            alarmViewModel.unRegisterAll(context,alarmList,repeating)

        }
    }

    private fun fabAlarmClicked(){
        binding.fabAlarm.setOnClickListener {
            binding.fabAlarm.collapse(true)
            binding.emptyImage.visibility=View.GONE
            binding.emptyListTxt.visibility=View.GONE
            val customDialog = CustomDialogChooseFragment(alarmViewModel)
            customDialog.show(parentFragmentManager, "m")

        }

    }

    private fun fetchAlarmItems(){
        alarmViewModel.fetchAlarmItems().observe(viewLifecycleOwner, Observer {

            alarmAdapter.setIncomingList(it)
            alarmList= it as MutableList<AlarmModel>
            if (it.isEmpty()){
                binding.emptyImage.visibility=View.VISIBLE
                binding.emptyListTxt.visibility=View.VISIBLE
                binding.fabAlarm.visibility=View.VISIBLE
                binding.alarmCheck.isChecked = false
            }
        })
    }

    private fun updateAlarmRecycler(){
        alarmAdapter=AlarmAdapter(ArrayList<AlarmModel>(), alarmViewModel,requireContext())
        binding.rvListWeatherHome.adapter = alarmAdapter
        binding.rvListWeatherHome.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.rvListWeatherHome.setHasFixedSize(true)
    }

    private fun timeInputCheckEvents(){
        binding.checkEventTimeTextInput.setOnClickListener(View.OnClickListener {
            calenderTime(binding.checkEventTimeTextInput,calenderEvent.time.hours,calenderEvent.time.minutes)

        })

    }

    private fun forNextHoursCheck(){
        binding.fornextradioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.R24hr) {
                repeating=24
            } else if (checkedId == R.id.R48hr) {
                repeating=48
            } else {
                repeating=72
            }
            Toast.makeText(requireContext(), "$repeating", Toast.LENGTH_SHORT).show()
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun switchAlarm(){
        binding.alarmCheck.setOnClickListener(View.OnClickListener {
            if (binding.checkEventTimeTextInput == null) {
                binding.alarmCheck.isChecked = false
                Toast.makeText(requireContext(), getString(R.string.enterTime), Toast.LENGTH_SHORT).show()
            } else if (alarmList == null || alarmList.isEmpty()) {
                Toast.makeText(requireContext(), getString(R.string.addAlertFirst), Toast.LENGTH_SHORT).show()
                binding.alarmCheck.isChecked = false
            } else if (binding.alarmCheck.isChecked) {
                Toast.makeText(requireContext(), getString(R.string.alarmon), Toast.LENGTH_SHORT).show()
                binding.imgAlarm.setBackgroundResource(R.drawable.alarm)
                binding.checkEventLayout.visibility = View.GONE
                binding.fornextradioGroup.visibility = View.GONE
                sharedPref.alarmSwitchedOn(true)
                binding.fabAlarm.visibility=View.GONE
                Toast.makeText(context,getString(R.string.addalarmperm), Toast.LENGTH_LONG).show()
                alarmViewModel.registerAll(context,alarmList,repeating,calenderEvent, activity as AppCompatActivity)
            } else {
                Toast.makeText(requireContext(), getString(R.string.alarmoff), Toast.LENGTH_SHORT).show()
                binding.imgAlarm.setBackgroundResource(R.drawable.alarm_off)
                binding.checkEventLayout.visibility = View.VISIBLE
                binding.fornextradioGroup.visibility = View.VISIBLE
                sharedPref.alarmSwitchedOn(false)
                binding.fabAlarm.visibility=View.VISIBLE
                alarmViewModel.unRegisterAll(context,alarmList,repeating)
            }
        })

    }

    private fun calenderTime(checkEventTimeTextInput: EditText, hour:Int,min:Int) {

        TimePickerDialog(requireContext(), object: TimePickerDialog.OnTimeSetListener{
            override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
                calenderEvent = Calendar.getInstance()
                calenderEvent.set(Calendar.HOUR_OF_DAY,p1)
                calenderEvent.set(Calendar.MINUTE,p2)
                calenderEvent.set(Calendar.SECOND,0)
                checkEventTimeTextInput.setText(SimpleDateFormat("HH:mm").format(calenderEvent.time))
                Log.i("timeinMilli","${calenderEvent.timeInMillis}")

            }
        }, hour, min, false).show()
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

/*
    private fun pickTime(editTxt: EditText) {
        calenderAlarm = Calendar.getInstance()

        val hour = calenderAlarm.get(Calendar.HOUR)
        val minute = calenderAlarm.get(Calendar.MINUTE)

        val mTimePicker2 = TimePickerDialog(
            requireContext(),
            TimePickerDialog.OnTimeSetListener { _, selectedHour: Int, selectedMinute: Int ->
                var hours = selectedHour
                var timeSet2 = ""
                when {
                    hours > 12 -> {
                        hours -= 12
                        timeSet2 = "PM"
                    }
                    hours == 0 -> {
                        hours += 12
                        timeSet2 = "AM"
                    }
                    hours == 12 -> {
                        timeSet2 = "PM"
                    }
                    else -> {
                        timeSet2 = "AM"
                    }
                }
                var min2 = ""
                min2 = if (selectedMinute < 10) "0$selectedMinute" else selectedMinute.toString()

                // Append in a StringBuilder

                var timePick = StringBuilder().append(hours).append(':')
                    .append(min2).append(" ").append(timeSet2).toString()
                editTxt.setText(timePick)

            },
            hour,
            minute,
            false
        )

        mTimePicker2.setTitle("Select Time")
        mTimePicker2.show()

    }
*/


}