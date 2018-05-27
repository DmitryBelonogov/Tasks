package com.nougust3.tasks.data.tasks

import io.reactivex.Observable

interface TaskDataSource {

    fun getTasks(): Observable<List<Task>>

    fun delete(task: Task)

    fun save(task: Task)

    fun update(task: Task)

    fun clear(time: Long)

}