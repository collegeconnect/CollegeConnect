package com.college.collegeconnect.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class MondayEntity(subjectName: String, startTime: String, startTimeShow: String, endTime: String, endTimeShow: String) :
        TimetableEntity(subjectName, startTime, startTimeShow, endTime, endTimeShow) {
    @PrimaryKey( autoGenerate = true )
    var id:Int = 0
}