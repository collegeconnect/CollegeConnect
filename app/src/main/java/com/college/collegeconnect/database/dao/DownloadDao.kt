package com.college.collegeconnect.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.college.collegeconnect.database.entity.DownloadEntity

@Dao
interface DownloadDao {

    @Insert
    suspend fun add(downloadEntity: DownloadEntity)

    @Delete
    suspend fun delete(downloadEntity: DownloadEntity)

    @Query("SELECT * FROM DownloadEntity")
    fun getDownloads():LiveData<ArrayList<DownloadEntity>>
}