package com.college.collegeconnect.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.college.collegeconnect.database.entity.TuesdayEntity
import com.college.collegeconnect.database.entity.WednesdayEntity

@Dao
interface WednesdayDao {

    @Insert
     suspend fun add(sub : WednesdayEntity)

    @Query("SELECT * FROM WednesdayEntity ORDER BY startTime ASC")
    fun getWedClasses(): LiveData<List<WednesdayEntity>>

    @Query("DELETE FROM WednesdayEntity WHERE id=:uid")
    suspend fun delete(uid:Int)

}