package com.nougust3.tasks.presentation

import android.databinding.DataBindingUtil
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.nougust3.tasks.R
import com.nougust3.tasks.data.tasks.Task
import com.nougust3.tasks.databinding.ActivityMainBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import org.koin.android.architecture.ext.viewModel

class MainActivity : AppCompatActivity() {

    private val model : TaskViewModel by viewModel()

    private lateinit var viewDataBinding: ActivityMainBinding
    private lateinit var adapter: TasksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewDataBinding.viewModel = model
        viewDataBinding.tasksView.layoutManager = LinearLayoutManager(this)
        viewDataBinding.viewModel?.load()?.subscribeOn(AndroidSchedulers.mainThread())?.observeOn(AndroidSchedulers.mainThread())?.subscribe { tasks ->
            run {
                adapter.update(tasks)
            }
        }
        viewDataBinding.addBtn.setOnClickListener {
            viewDataBinding.viewModel?.save()
        }

        viewDataBinding.appTitle.typeface = Typeface.createFromAsset(assets, "fonts/Comfortaa-Bold.ttf")
        viewDataBinding.taskEdit.typeface = Typeface.createFromAsset(assets, "fonts/Comfortaa-Bold.ttf")

        setupAdapter()
    }

    override fun onResume() {
        super.onResume()
        viewDataBinding.viewModel?.update()
    }

    private fun setupAdapter() {
        val viewModel = viewDataBinding.viewModel

        if(viewModel != null) {
            adapter = TasksAdapter(this, callback = object : DiffUtil.ItemCallback<Task>() {
                override fun areContentsTheSame(oldItem: Task?, newItem: Task?): Boolean {
                    return oldItem!!.name == newItem!!.name
                            && oldItem.isComplete == newItem.isComplete
                }

                override fun areItemsTheSame(oldItem: Task?, newItem: Task?): Boolean {
                    return oldItem!!.time == newItem!!.time
                }
            }, taskViewModel = viewModel)
            viewDataBinding.tasksView.adapter = adapter
            viewDataBinding.tasksView.setItemViewCacheSize(20)
            viewDataBinding.tasksView.isDrawingCacheEnabled = true
            viewDataBinding.tasksView.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        }
    }
}
