package com.college.collegeconnect.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class TuesdayEntity(subjectName: String, startTime: String, startTimeShow: String, endTime: String, endTimeShow: String, roomNumber: String) :
        TimetableEntity(subjectName, startTime, startTimeShow, endTime, endTimeShow, roomNumber) {
    @PrimaryKey( autoGenerate = true )
    var id:Int = 0
}