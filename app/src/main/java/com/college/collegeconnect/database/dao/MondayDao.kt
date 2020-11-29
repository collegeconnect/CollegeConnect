package com.college.collegeconnect.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.college.collegeconnect.database.entity.MondayEntity

@Dao
interface MondayDao {

    @Insert
     suspend fun add(sub : MondayEntity)

    @Query("SELECT * FROM MondayEntity ORDER BY startTime ASC")
    fun getMonClasses(): LiveData<List<MondayEntity>>

    @Delete
    suspend fun delete(sub: MondayEntity)

}