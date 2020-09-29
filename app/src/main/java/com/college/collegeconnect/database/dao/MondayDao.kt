package com.college.collegeconnect.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.college.collegeconnect.database.entity.MondayEntity

@Dao
interface MondayDao {

    @Insert
     fun add(sub : MondayEntity)

    @Query("SELECT * FROM MondayEntity")
    fun getMonClasses(): LiveData<List<MondayEntity>>
}