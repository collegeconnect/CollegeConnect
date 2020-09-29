package com.college.collegeconnect.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.college.collegeconnect.database.dao.DownloadDao
import com.college.collegeconnect.database.entity.DownloadEntity

@Database(
        entities = [DownloadEntity::class],
        version = 1
)
abstract class DownloadDatabase : RoomDatabase() {
    abstract fun getDownloadsDao(): DownloadDao

    companion object {

        @Volatile
        private var instance: DownloadDatabase? = null

        private val LOCK = Any()

        operator fun invoke(context: Context) = instance
                ?: synchronized(LOCK) {
                    instance ?: buildDatabase(context).also {
                        instance = it
                    }
                }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
                context.applicationContext,
                DownloadDatabase::class.java,
                "DownloadDatabase"
        ).build()
    }
}