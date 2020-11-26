package com.college.collegeconnect.timetable

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.college.collegeconnect.database.TimeTableDatabase
import com.college.collegeconnect.database.entity.*
import kotlinx.coroutines.launch

class NewTimeTableViewModel(application: Application):AndroidViewModel(application) {

    fun addItem(name: String, startTime: String, startTimeShow: String, endTime: String, endTimeShow: String, position: Int){
        viewModelScope.launch {
            when (position) {
                0 -> TimeTableDatabase(getApplication()).getMondayDao().add(MondayEntity(name,startTime, startTimeShow,endTime, endTimeShow))
                1 -> TimeTableDatabase(getApplication()).getTuesdayDao().add(TuesdayEntity(name,startTime, startTimeShow,endTime, endTimeShow))
                2 -> TimeTableDatabase(getApplication()).getWednesdayDao().add(WednesdayEntity(name,startTime, startTimeShow,endTime, endTimeShow))
                3 -> TimeTableDatabase(getApplication()).getThursdayDao().add(ThursdayEntity(name,startTime, startTimeShow,endTime, endTimeShow))
                4 -> TimeTableDatabase(getApplication()).getFridayDao().add(FridayEntity(name,startTime, startTimeShow,endTime, endTimeShow))
                5 -> TimeTableDatabase(getApplication()).getSaturdayDao().add(SaturdayEntity(name,startTime, startTimeShow,endTime, endTimeShow))
                6 -> TimeTableDatabase(getApplication()).getSundayDao().add(SundayEntity(name,startTime, startTimeShow,endTime, endTimeShow))
            }
        }
    }
}