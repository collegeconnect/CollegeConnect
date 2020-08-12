package com.college.collegeconnect.database

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
    suspend fun getAttendance(): List<SubjectDetails>
}