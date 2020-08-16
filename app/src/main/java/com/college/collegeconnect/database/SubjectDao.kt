package com.college.collegeconnect.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SubjectDao {

    @Insert
    suspend fun add(subject: SubjectDetails)

    @Update
    suspend fun update(subject: SubjectDetails)

    @Delete
    suspend fun delete(subject: SubjectDetails)

    @Query("SELECT * FROM SubjectDetails ORDER BY ID ASC")
    fun getAttendance(): LiveData<List<SubjectDetails>>

    @Query("SELECT SUM(attended) FROM SubjectDetails")
    suspend fun getAttended():Int

    @Query("SELECT SUM(missed) FROM SubjectDetails")
    suspend fun getMissed():Int
}