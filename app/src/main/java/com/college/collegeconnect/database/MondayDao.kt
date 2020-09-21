package com.college.collegeconnect.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MondayDao {

    @Insert
     fun add(sub : MondayEntity)

    @Query("SELECT * FROM MondayEntity")
    fun getMonClasses(): LiveData<List<MondayEntity>>
}