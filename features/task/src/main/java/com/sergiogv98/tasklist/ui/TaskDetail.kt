package com.sergiogv98.tasklist.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.moronlu18.accounts.entity.Task
import com.moronlu18.tasklist.databinding.FragmentTaskDetailBinding
import com.sergiogv98.tasklist.adapter.TaskAdapter
import com.sergiogv98.usecase.TaskDetailViewModel


class TaskDetail : Fragment() {

    private var _binding: FragmentTaskDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TaskDetailViewModel by viewModels()
    private val args: TaskDetailArgs by navArgs()
    private lateinit var adapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTaskDetailBinding.inflate(inflater, container, false)
        binding.viewmodel = this.viewModel
        binding.lifecycleOwner = this

        var taskMutableList: MutableList<Task> = viewModel.getTaskDataSet()

        adapter = TaskAdapter(
            taskList = taskMutableList,
        )

        val task: Task = args.task
        binding.taskDetailsClientImageView.setImageResource(viewModel.getCustomerPhoto(task.clientID))
        binding.taskDetailsClientNameTxt.text = viewModel.getCustomerName(task.clientID)
        binding.taskDetailsTaskName.text = task.nomTask
        binding.taskDetailsStatusButton.text = task.taskStatus.toString().replaceRange(1, task.taskStatus.toString().length, task.taskStatus.toString().substring(1).lowercase())
        binding.taskDetailsTaskTypeName.text = task.typeTask.toString().replaceRange(1, task.typeTask.toString().length, task.typeTask.toString().substring(1).lowercase())
        binding.taskDetailsDateCreation.text = task.fechCreation.toString().substring(0, task.fechCreation.toString().lastIndexOf("T"))
        binding.taskDetailsDateEnd.text = task.fechFinalization.toString().substring(0, task.fechFinalization.toString().lastIndexOf("T"))
        binding.taskDetailsDescription.text = task.descTask

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.taskDetailsButtonBack.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}