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
     suspend fun add(sub : ThursdayEntity)

    @Query("SELECT * FROM ThursdayEntity ORDER BY startTime ASC")
    fun getThursClasses(): LiveData<List<ThursdayEntity>>

    @Query("DELETE FROM ThursdayEntity WHERE id=:uid")
    suspend fun delete(uid:Int)

}