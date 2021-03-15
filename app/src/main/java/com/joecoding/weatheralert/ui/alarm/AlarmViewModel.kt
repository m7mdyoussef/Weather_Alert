package com.joecoding.weatheralert.ui.alarm

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.joecoding.weatheralert.model.currentWeatherModel.db.localSourceDB.pojo.alarmModel.AlarmModel
import com.joecoding.weatheralert.model.currentWeatherModel.db.localSourceDB.pojo.favoritePlacesModel.FavoriteModel
import com.joecoding.weatheralert.model.repository.Repository
import java.util.*

class AlarmViewModel (application: Application) : AndroidViewModel(application){
    private val ONE_DAY_IN_SECONDS = 86400000
    private val TWO_DAYS_IN_SECONDS = 172800000
    private val repository: Repository = Repository(application)

    //recyclerView Data handling
    fun insertAlarmToDB(alarmModel: AlarmModel) {
        repository.insertAlarm(alarmModel)
    }

    fun fetchAlarmItems() : LiveData<List<AlarmModel>> {
        return repository.retrieveAlarmData()
    }

    fun deleteAlarmItem(id: Int){
        repository.deleteAlarm(id)
    }



    @RequiresApi(Build.VERSION_CODES.M)
    fun registerAll(context: Context?, alarmList:List<AlarmModel>, hourDuration:Int, calenderEvent:Calendar, activity: FragmentActivity) {
        val alarmIntent = Intent(context,AlarmBCR::class.java)
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        for (item in alarmList){
            if(Calendar.getInstance().timeInMillis >= calenderEvent.timeInMillis){
                alarmIntent.putExtra("ITEM_ID",item.id)
                var time = calenderEvent.timeInMillis
                calenderEvent.timeInMillis=time.plus(ONE_DAY_IN_SECONDS)
                var pendingIntent= PendingIntent.getBroadcast(context,item.id,alarmIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT)
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calenderEvent.timeInMillis,pendingIntent)
                Log.d("taaaaaaaaaaaaaaageee24", "${calenderEvent.timeInMillis} llllllll")

                if (hourDuration  == 48){
                    pendingIntent= PendingIntent.getBroadcast(context,item.id+2000,alarmIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT)
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calenderEvent.timeInMillis+ONE_DAY_IN_SECONDS,pendingIntent)
                    Log.d("48", "${calenderEvent.timeInMillis+ONE_DAY_IN_SECONDS}one")
                }else if(hourDuration  == 72){
                    pendingIntent= PendingIntent.getBroadcast(context,item.id+2000,alarmIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT)
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calenderEvent.timeInMillis+ONE_DAY_IN_SECONDS,pendingIntent)
                    Log.d("48", "${calenderEvent.timeInMillis+ONE_DAY_IN_SECONDS}two")

                    pendingIntent= PendingIntent.getBroadcast(context,item.id+4000,alarmIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT)
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calenderEvent.timeInMillis+TWO_DAYS_IN_SECONDS,pendingIntent)
                    Log.d("72", "${calenderEvent.timeInMillis+TWO_DAYS_IN_SECONDS}three")
                }
            }else{

                alarmIntent.putExtra("ITEM_ID",item.id)
                var pendingIntent= PendingIntent.getBroadcast(context,item.id,alarmIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT)
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calenderEvent.timeInMillis,pendingIntent)
                Log.d("taaaaaaaaaaaaaaageee24", "${calenderEvent.timeInMillis}four")

                if (hourDuration  == 48){
                    pendingIntent= PendingIntent.getBroadcast(context,item.id+2000,alarmIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT)
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calenderEvent.timeInMillis+ONE_DAY_IN_SECONDS,pendingIntent)
                    Log.d("48", "${calenderEvent.timeInMillis+ONE_DAY_IN_SECONDS}five")
                }else if(hourDuration  == 72){
                    pendingIntent= PendingIntent.getBroadcast(context,item.id+2000,alarmIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT)
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calenderEvent.timeInMillis+ONE_DAY_IN_SECONDS,pendingIntent)
                    Log.d("48", "${calenderEvent.timeInMillis+ONE_DAY_IN_SECONDS}six")

                    pendingIntent= PendingIntent.getBroadcast(context,item.id+4000,alarmIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT)
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calenderEvent.timeInMillis+TWO_DAYS_IN_SECONDS,pendingIntent)
                    Log.d("72", "${calenderEvent.timeInMillis+TWO_DAYS_IN_SECONDS}seven")
                }

            }
        }
    }


     fun unRegisterAll(context: Context?,alarmList:List<AlarmModel>,hourDuration:Int) {
        for (item in alarmList){
            val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val alarmIntent = Intent(context,AlarmBCR::class.java)
            var pendingIntent= PendingIntent.getBroadcast(context,item.id,alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)
            if(alarmManager != null){
                alarmManager.cancel(pendingIntent)
            }

            if (hourDuration == 72){
                pendingIntent= PendingIntent.getBroadcast(context,item.id+2000,alarmIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT)
                if(alarmManager != null){
                    alarmManager.cancel(pendingIntent)
                }
                pendingIntent= PendingIntent.getBroadcast(context,item.id+4000,alarmIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT)
                if(alarmManager != null){
                    alarmManager.cancel(pendingIntent)
                }
            }else if(hourDuration == 48){
                pendingIntent= PendingIntent.getBroadcast(context,item.id+2000,alarmIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT)
                if(alarmManager != null){
                    alarmManager.cancel(pendingIntent)
                }
            }
        }
    }

}