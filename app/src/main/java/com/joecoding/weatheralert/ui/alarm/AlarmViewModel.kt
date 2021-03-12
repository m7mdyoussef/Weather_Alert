package com.joecoding.weatheralert.ui.alarm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.joecoding.weatheralert.model.currentWeatherModel.db.localSourceDB.pojo.alarmModel.AlarmModel
import com.joecoding.weatheralert.model.currentWeatherModel.db.localSourceDB.pojo.favoritePlacesModel.FavoriteModel
import com.joecoding.weatheralert.model.repository.Repository

class AlarmViewModel (application: Application) : AndroidViewModel(application){
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
}