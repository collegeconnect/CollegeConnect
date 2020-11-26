package com.college.collegeconnect.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.college.collegeconnect.database.entity.ThursdayEntity
import com.college.collegeconnect.database.entity.TuesdayEntity

@Dao
interface ThursdayDao {

    @Insert
     fun add(sub : ThursdayEntity)

    @Query("SELECT * FROM ThursdayEntity")
    fun getThursClasses(): LiveData<List<ThursdayEntity>>
}