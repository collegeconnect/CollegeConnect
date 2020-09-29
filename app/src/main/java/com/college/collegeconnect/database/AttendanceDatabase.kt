package com.college.collegeconnect.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.college.collegeconnect.database.dao.SubjectDao
import com.college.collegeconnect.database.entity.SubjectDetails

@Database(
        entities = [SubjectDetails::class],
        version = 1
)
abstract class AttendanceDatabase : RoomDatabase() {
    abstract fun getAttendanceDao(): SubjectDao

    companion object {

        @Volatile
        private var instance: AttendanceDatabase? = null

        private val LOCK = Any()

        operator fun invoke(context: Context) = instance
                ?: synchronized(LOCK) {
                    instance ?: buildDatabase(context).also {
                        instance = it
                    }
                }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
                context.applicationContext,
                AttendanceDatabase::class.java,
                "AttendanceDatabase"
        ).build()

    }
}