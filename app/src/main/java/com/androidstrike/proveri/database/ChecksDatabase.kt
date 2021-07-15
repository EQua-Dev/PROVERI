package com.androidstrike.proveri.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DBModel::class], version = 1, exportSchema = false)
abstract class ChecksDatabase : RoomDatabase() {
    abstract fun checksDao(): ChecksDao

    companion object {
        @Volatile //makes the db immediately readable to other classes
        private var INSTANCE: ChecksDatabase? = null
        private val LOCK = Any()

        fun invokeDB(context: Context): ChecksDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                //return the intance if it already exists (to avoid creating multiple instances
                return tempInstance
            }
            synchronized(this) { //synchronized makes the db protected from multiple concurrent executions from various threads
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ChecksDatabase::class.java,
                    "saved_checks"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}