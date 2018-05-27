package com.nougust3.tasks.data.tasks

import android.arch.persistence.room.*
import com.nougust3.tasks.data.tasks.Task
import io.reactivex.Flowable

@Dao
interface TaskDao {

    @Query("SELECT * FROM Tasks")
    fun getTasks(): Flowable<List<Task>>

    @Insert
    fun save(task: Task)

    @Update
    fun update(task: Task)

    @Delete
    fun delete(task: Task)

    @Delete
    fun clear(tasks: List<Task>)

}