package com.college.collegeconnect.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.college.collegeconnect.database.entity.DownloadEntity

@Dao
interface DownloadDao {

    @Insert
    suspend fun add(downloadEntity: DownloadEntity)

    @Query("DELETE FROM DownloadEntity WHERE id = :id")
    suspend fun delete(id:Int)

    @Query("SELECT * FROM DownloadEntity")
    fun getDownloadFiles():LiveData<List<DownloadEntity>>
}