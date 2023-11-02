package com.sergiogv98.tasklist

import Task
import TaskProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sergiogv98.tasklist.adapter.TaskAdapter
import com.moronlu18.tasklist.databinding.FragmentTaskListBinding


class TaskList : Fragment() {

    private lateinit var binding : FragmentTaskListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaskListBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fab = requireActivity().findViewById<FloatingActionButton>(com.moronlu18.invoice.R.id.fab)
        fab.visibility = View.GONE

        initRecyclerViewTask()
    }

    private fun initRecyclerViewTask(){

        val manager = LinearLayoutManager(requireContext())
        val decoration = DividerItemDecoration(requireContext(),manager.orientation)
        binding.taskListRecyclerTasks.layoutManager = manager

        binding.taskListRecyclerTasks.adapter = TaskAdapter(TaskProvider.taskList){

                x-> onItemSelected(x)
        }

        binding.taskListRecyclerTasks.addItemDecoration(decoration)

    }
    fun onItemSelected(task: Task) {
        Toast.makeText(requireContext(),task.nomTask, Toast.LENGTH_SHORT).show()
    }
}