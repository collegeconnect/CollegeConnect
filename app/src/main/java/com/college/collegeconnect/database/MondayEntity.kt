package com.college.collegeconnect.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MondayEntity(
        val subjectName: String,
        val startTime: String,
        val endTime: String
) {
    @PrimaryKey( autoGenerate = true )
    var id:Int = 0
}