package com.college.collegeconnect.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.college.collegeconnect.database.dao.*
import com.college.collegeconnect.database.entity.*

@Database(
        entities = [MondayEntity::class, TuesdayEntity::class, WednesdayEntity::class, ThursdayEntity::class, FridayEntity::class, SaturdayEntity::class, SundayEntity::class],
        version = 1
)
abstract class TimeTableDatabase : RoomDatabase(){

    abstract fun getMondayDao(): MondayDao
    abstract fun getTuesdayDao(): TuesdayDao
    abstract fun getWednesdayDao(): WednesdayDao
    abstract fun getThursdayDao(): ThursdayDao
    abstract fun getFridayDao(): FridayDao
    abstract fun getSaturdayDao(): SaturdayDao
    abstract fun getSundayDao(): SundayDao

    companion object {
        @Volatile private var instance : TimeTableDatabase? = null

        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance?: builDatabase(context).also {
                instance = it
            }
        }

        private fun builDatabase(context: Context) = Room.databaseBuilder(
                context.applicationContext,
                TimeTableDatabase::class.java,
                "timetabledatabase"
        ).build()
    }
}