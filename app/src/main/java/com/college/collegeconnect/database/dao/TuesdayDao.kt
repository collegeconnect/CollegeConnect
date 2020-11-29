package com.college.collegeconnect.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.college.collegeconnect.database.entity.TuesdayEntity

@Dao
interface TuesdayDao {

    @Insert
     suspend fun add(sub : TuesdayEntity)

    @Query("SELECT * FROM TuesdayEntity ORDER BY startTime ASC")
    fun getTuesClasses(): LiveData<List<TuesdayEntity>>

    @Query("DELETE FROM TuesdayEntity WHERE id=:uid")
    suspend fun delete(uid:Int)

}