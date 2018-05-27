package com.nougust3.tasks.presentation

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.databinding.ObservableField
import com.nougust3.tasks.data.tasks.Task
import com.nougust3.tasks.domain.TasksInteractor
import io.reactivex.Observable

class TaskViewModel(
        context: Application,
        private val tasksInteractor: TasksInteractor
): AndroidViewModel(context) {

    private val taskAddEvent = SingleLiveEvent<Void>()

    val name: ObservableField<String> = ObservableField()

    fun load(): Observable<List<Task>> {
        return tasksInteractor.getTasks()
    }

    fun save() {
        val taskName = name.get()

        if(taskName!!.isNotEmpty()) {
            tasksInteractor.save(Task(taskName, System.currentTimeMillis()))
            taskAddEvent.call()
            name.set("")
        }
    }

    fun complete(task: Task) {
        task.isComplete = !task.isComplete
        tasksInteractor.update(task)
    }

    fun delete(task: Task) {
        tasksInteractor.delete(task)
    }

    fun update() {
        tasksInteractor.update()
    }
}