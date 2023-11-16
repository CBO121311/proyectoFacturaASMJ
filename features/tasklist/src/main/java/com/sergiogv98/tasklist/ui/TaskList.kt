package com.sergiogv98.tasklist.ui

import Task
import TaskProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sergiogv98.tasklist.adapter.TaskAdapter
import com.moronlu18.tasklist.databinding.FragmentTaskListBinding


class TaskList : Fragment() {


    private var _binding:FragmentTaskListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskListBinding.inflate(inflater,container,false)

        binding.taskListAddTask.setOnClickListener {
            findNavController().navigate(com.moronlu18.invoice.R.id.action_TaskListFragment_to_TaskCreationFragment)
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        findNavController().navigate(com.moronlu18.invoice.R.id.action_TaskListFragment_to_TaskDetailFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}