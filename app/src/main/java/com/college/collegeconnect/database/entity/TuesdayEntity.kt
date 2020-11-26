package com.college.collegeconnect.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class TuesdayEntity(subjectName: String, startTime: String, endTime: String) : TimetableEntity(subjectName, startTime, endTime) {
    @PrimaryKey( autoGenerate = true )
    var id:Int = 0
}