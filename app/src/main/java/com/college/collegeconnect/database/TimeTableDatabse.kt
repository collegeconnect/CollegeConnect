package com.college.collegeconnect.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.college.collegeconnect.database.dao.MondayDao
import com.college.collegeconnect.database.entity.MondayEntity

@Database(
        entities = [MondayEntity::class],
        version = 1
)
abstract class TimeTableDatabse : RoomDatabase(){

    abstract fun getMondayDao(): MondayDao

    companion object {
        @Volatile private var instance : TimeTableDatabse? = null

        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance?: builDatabase(context).also {
                instance = it
            }
        }

        private fun builDatabase(context: Context) = Room.databaseBuilder(
                context.applicationContext,
                TimeTableDatabse::class.java,
                "timetabledatabse"
        ).build()
    }
}