package com.college.collegeconnect.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.college.collegeconnect.database.entity.TuesdayEntity

@Dao
interface TuesdayDao {

    @Insert
     fun add(sub : TuesdayEntity)

    @Query("SELECT * FROM TuesdayEntity")
    fun getTuesClasses(): LiveData<List<TuesdayEntity>>
}