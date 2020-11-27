package com.college.collegeconnect.timetable

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.college.collegeconnect.database.TimeTableDatabase
import com.college.collegeconnect.database.entity.*
import kotlinx.coroutines.launch

class NewTimeTableViewModel(application: Application):AndroidViewModel(application) {

    fun addItem(name: String, startTime: String, startTimeShow: String, endTime: String, endTimeShow: String, position: Int, roomNumber: String){
        viewModelScope.launch {
            when (position) {
                0 -> TimeTableDatabase(getApplication()).getMondayDao().add(MondayEntity(name,startTime, startTimeShow,endTime, endTimeShow,roomNumber))
                1 -> TimeTableDatabase(getApplication()).getTuesdayDao().add(TuesdayEntity(name,startTime, startTimeShow,endTime, endTimeShow,roomNumber))
                2 -> TimeTableDatabase(getApplication()).getWednesdayDao().add(WednesdayEntity(name,startTime, startTimeShow,endTime, endTimeShow,roomNumber))
                3 -> TimeTableDatabase(getApplication()).getThursdayDao().add(ThursdayEntity(name,startTime, startTimeShow,endTime, endTimeShow,roomNumber))
                4 -> TimeTableDatabase(getApplication()).getFridayDao().add(FridayEntity(name,startTime, startTimeShow,endTime, endTimeShow,roomNumber))
                5 -> TimeTableDatabase(getApplication()).getSaturdayDao().add(SaturdayEntity(name,startTime, startTimeShow,endTime, endTimeShow,roomNumber))
                6 -> TimeTableDatabase(getApplication()).getSundayDao().add(SundayEntity(name,startTime, startTimeShow,endTime, endTimeShow,roomNumber))
            }
        }
    }

    fun deleteClass(position: Int, entity: TimetableEntity) {
        viewModelScope.launch {
            when (position) {
                0 -> TimeTableDatabase(getApplication()).getMondayDao().delete((entity as MondayEntity))
                1 -> TimeTableDatabase(getApplication()).getTuesdayDao().delete((entity as TuesdayEntity).id)
                2 -> TimeTableDatabase(getApplication()).getWednesdayDao().delete((entity as WednesdayEntity).id)
                3 -> TimeTableDatabase(getApplication()).getThursdayDao().delete((entity as ThursdayEntity).id)
                4 -> TimeTableDatabase(getApplication()).getFridayDao().delete((entity as FridayEntity).id)
                5 -> TimeTableDatabase(getApplication()).getSaturdayDao().delete((entity as SaturdayEntity).id)
                6 -> TimeTableDatabase(getApplication()).getSundayDao().delete((entity as SundayEntity).id)
            }
        }
    }
}