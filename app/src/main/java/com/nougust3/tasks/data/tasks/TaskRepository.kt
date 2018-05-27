package com.nougust3.tasks.data.tasks

import com.nougust3.tasks.data.tasks.local.TaskLocalDataSource
import io.reactivex.Observable
import io.reactivex.subjects.ReplaySubject

class TaskRepository(private val localDataSource: TaskLocalDataSource) : TaskDataSource {

    private var tasks: List<Task> = ArrayList()
    private val tasksSubject = ReplaySubject.create<List<Task>>()

    override fun delete(task: Task) {
        tasks.filter { t: Task -> t.time != task.time }
        tasksSubject.onNext(tasks)
        localDataSource.delete(task)
    }

    override fun getTasks(): Observable<List<Task>> {
        return tasksSubject
    }

    fun getTasksCache(): List<Task> {
        return tasks
    }

    fun clearTasks(time: Long) {
        localDataSource.clear(time);
    }

    override fun save(task: Task) {
        with(tasks as ArrayList) {
            add(task)
        }
        tasksSubject.onNext(tasks)
        localDataSource.save(task)
    }

    override fun update(task: Task) {
        tasks.forEach { t: Task -> if(t.time == task.time) t.isComplete = task.isComplete }
        tasksSubject.onNext(tasks)
        localDataSource.update(task)
    }

    init {
        localDataSource.getTasks()
                .doOnNext { tasks ->
                    run {
                        this.tasks = tasks
                        tasksSubject.onNext(tasks)
                    }
                }.subscribe()
    }

}