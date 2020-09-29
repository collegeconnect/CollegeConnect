package com.college.collegeconnect.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DownloadEntity(
        val docName:String,
        val authName:String,
        val unit:String,
) {
    @PrimaryKey( autoGenerate = true )
    var id:Int = 0
}