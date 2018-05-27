package com.nougust3.tasks.presentation

import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView
import com.nougust3.tasks.data.tasks.Task

object TasksListBindings {

    @BindingAdapter("app:items")
    @JvmStatic
    fun setItems(listView: RecyclerView, items: List<Task>) {
        with(listView.adapter as TasksAdapter) {
            update(items)
        }
    }

}