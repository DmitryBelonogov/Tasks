package com.nougust3.tasks.di

import android.arch.persistence.room.Database
import android.content.Context
import com.nougust3.tasks.Tasks
import com.nougust3.tasks.data.tasks.TaskDao
import com.nougust3.tasks.data.tasks.TaskDataSource
import com.nougust3.tasks.data.tasks.TaskRepository
import com.nougust3.tasks.data.tasks.local.TaskLocalDataSource
import com.nougust3.tasks.data.tasks.local.TasksDatabase
import com.nougust3.tasks.domain.TasksInteractor
import com.nougust3.tasks.presentation.TaskViewModel
import com.nougust3.tasks.utils.rx.AppSchedulerProvider
import com.nougust3.tasks.utils.rx.SchedulerProvider
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.applicationContext

val appModule = applicationContext {

    viewModel {
        TaskViewModel(get(), get())
    }

    bean {
        TasksInteractor(get())
    }

    bean {
        TaskRepository(get())
    }

    bean {
        TaskLocalDataSource(get())
    }

}

val rxModule = applicationContext {

    bean {
        AppSchedulerProvider() as SchedulerProvider
    }

}

val app = listOf(appModule, rxModule)

fun getTaskDao() : TaskDao {
    return getDatabase().taskDao()
}

fun getDatabase() : TasksDatabase {
    return TasksDatabase.getInstance(getContext())
}

fun getContext() : Context {
    return Tasks.appContext
}