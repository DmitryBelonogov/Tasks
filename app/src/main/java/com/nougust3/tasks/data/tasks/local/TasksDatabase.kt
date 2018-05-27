package com.nougust3.tasks.data.tasks.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.nougust3.tasks.data.tasks.Task
import com.nougust3.tasks.data.tasks.TaskDao

@Database(entities = [(Task::class)], version = 1)
abstract class TasksDatabase: RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object {

        private var INSTANCE: TasksDatabase? = null

        private val lock = Any()

        fun getInstance(context: Context): TasksDatabase {
            synchronized(lock) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            TasksDatabase::class.java, "Tasks.db"
                    ).build()
                }

                return INSTANCE!!
            }
        }

    }

}