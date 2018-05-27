package com.nougust3.tasks.domain

import com.nougust3.tasks.data.tasks.Task
import com.nougust3.tasks.data.tasks.TaskRepository
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class TasksInteractor(
        private val repository: TaskRepository
) {

    private val tasksSubject = PublishSubject.create<List<Task>>()

    fun getTasks(): PublishSubject<List<Task>> {
        return tasksSubject
    }

    fun save(task: Task) {
        repository.save(task)
    }   

    fun update(task: Task) {
        repository.update(task)
    }

    fun delete(task: Task) {
        repository.delete(task)
    }

    private fun actual(tasks: List<Task>): List<Task> {
        val time = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)
        return tasks.filter { it.time > time }.reversed()
    }

    fun update() {
        tasksSubject.onNext(actual(repository.getTasksCache()))
    }

    init {
        repository.getTasks()
                .subscribe {
                    tasksSubject.onNext(actual(it))
                }
        Observable.interval(1, TimeUnit.MINUTES)
                .subscribe {
                    tasksSubject.onNext(actual(repository.getTasksCache()))
                }
        repository.clearTasks(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1))
    }

}