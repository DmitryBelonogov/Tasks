package com.nougust3.tasks.data.tasks.local

import android.content.Context
import com.nougust3.tasks.data.tasks.Task
import com.nougust3.tasks.data.tasks.TaskDao
import com.nougust3.tasks.data.tasks.TaskDataSource
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TaskLocalDataSource(
        context: Context
): TaskDataSource {

    private val taskDao = TasksDatabase.getInstance(context).taskDao()

    override fun update(task: Task) {
        Observable.just(task)
                .subscribeOn(Schedulers.io())
                .subscribe { taskDao.update(task) }
    }

    override fun delete(task: Task) {
        Observable.just(task)
                .subscribeOn(Schedulers.io())
                .subscribe { taskDao.delete(task) }
    }

    override fun getTasks(): Observable<List<Task>> {
        return taskDao.getTasks()
                .toObservable()
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
    }

    override fun save(task: Task) {
        Observable.just(task)
                .subscribeOn(Schedulers.io())
                .subscribe { taskDao.save(task) }
    }

    override fun clear(time: Long) {
        getTasks().subscribe {
            taskDao.clear(it.filter { it.time > time })
        }
    }

}