package com.sergiogv98.taskdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.moronlu18.accounts.entity.Task
import com.moronlu18.accounts.enum.TypeTask
import com.moronlu18.tasklist.databinding.FragmentTaskDetailBinding


class TaskDetail : Fragment() {

    private var _binding: FragmentTaskDetailBinding? = null
    private val binding get() = _binding!!
    private val args: TaskDetailArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTaskDetailBinding.inflate(inflater, container, false)
        val tarea: Task = args.task

        binding.taskDetailsClientNameTxt.text = tarea.nomClient
        binding.taskDetailsTaskName.text = tarea.nomTask
        binding.taskDetailsStatusButton.text = tarea.taskStatus.toString().replaceRange(1, tarea.taskStatus.toString().length, tarea.taskStatus.toString().substring(1).lowercase())
        binding.taskDetailsTaskTypeName.text = tarea.typeTask.toString().replaceRange(1, tarea.typeTask.toString().length, tarea.typeTask.toString().substring(1).lowercase())
        binding.taskDetailsDateCreation.text = tarea.fechCreation
        binding.taskDetailsDateEnd.text = tarea.fechFinalization
        binding.taskDetailsDescription.text = tarea.descTask

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