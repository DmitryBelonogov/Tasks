package com.nougust3.tasks.data.tasks

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task constructor(
        @ColumnInfo(name = "name")
        var name: String = "Task name",

        @PrimaryKey
        @ColumnInfo(name = "time")
        var time: Long = 0
) {

    @ColumnInfo(name = "complete")
    var isComplete = false

    val isDone
        get() = time - System.currentTimeMillis() > 24 * 60 * 60 * 1000

}