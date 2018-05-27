package com.nougust3.tasks.presentation

import android.content.Context
import android.graphics.Typeface
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import com.nougust3.tasks.R
import com.nougust3.tasks.data.tasks.Task
import kotlinx.android.synthetic.main.task_item.view.*
import java.util.concurrent.TimeUnit

class TasksAdapter(
        private val context: Context,
        callback: DiffUtil.ItemCallback<Task>,
        private val taskViewModel: TaskViewModel
) : ListAdapter<Task, TasksAdapter.TaskViewHolder>(callback) {

    private var tasks: List<Task> = ArrayList()

    fun update(tasks: List<Task>) {
        this.tasks = tasks
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): Task {
        return tasks[position]
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position)
        val isComplete = task.isComplete

        holder.name.text = task.name
        holder.name.alpha = if(isComplete) 0.5f else 1f
        holder.time.alpha = if(isComplete) 0.5f else 1f
        holder.time.text = if(isComplete) "Done" else getTime(task.time)
        holder.checkBox.isChecked = isComplete

        holder.checkBox.setOnClickListener { taskViewModel.complete(task) }
        holder.cancelBtn.setOnClickListener { taskViewModel.delete(task) }
    }

    private fun getTime(time: Long): String {
        val currentTime = System.currentTimeMillis() - time
        val hours = TimeUnit.MILLISECONDS.toHours(currentTime)
        val minutes = currentTime - TimeUnit.HOURS.toMillis(hours)

        return String.format("Left %d hours %d minutes",
                23 - hours,
                60 - TimeUnit.MILLISECONDS.toMinutes(minutes))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(LayoutInflater.from(context).inflate(R.layout.task_item, parent, false))
    }

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.name_view!!
        val time = view.time_view!!
        val checkBox = view.checkbox!!
        val cancelBtn = view.cancel_btn!!

        init {
            name.typeface = Typeface.createFromAsset(view.context.assets, "fonts/Comfortaa-Bold.ttf")
            time.typeface = Typeface.createFromAsset(view.context.assets, "fonts/Comfortaa-Light.ttf")
        }
    }

}