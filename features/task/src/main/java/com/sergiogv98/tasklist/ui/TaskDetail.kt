package com.sergiogv98.tasklist.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.moronlu18.accounts.entity.Task
import com.moronlu18.accounts.repository.CustomerProvider
import com.moronlu18.accounts.repository.TaskProvider
import com.moronlu18.invoice.base.BaseFragmentDialog
import com.moronlu18.tasklist.R
import com.moronlu18.tasklist.databinding.FragmentTaskDetailBinding
import com.sergiogv98.tasklist.adapter.TaskAdapter


class TaskDetail : Fragment() {

    private var _binding: FragmentTaskDetailBinding? = null
    private var taskMutableList: MutableList<Task> = TaskProvider.taskDataSet
    private val binding get() = _binding!!
    private val args: TaskDetailArgs by navArgs()
    private lateinit var adapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        adapter = TaskAdapter(
            taskList = taskMutableList,
        )

        _binding = FragmentTaskDetailBinding.inflate(inflater, container, false)
        val tarea: Task = args.task
        binding.taskDetailsClientImageView.setImageResource(CustomerProvider.getCustomerPhotoById(tarea.clientID))
        binding.taskDetailsClientNameTxt.text = CustomerProvider.getCustomerNameById(tarea.clientID)
        binding.taskDetailsTaskName.text = tarea.nomTask
        binding.taskDetailsStatusButton.text = tarea.taskStatus.toString().replaceRange(1, tarea.taskStatus.toString().length, tarea.taskStatus.toString().substring(1).lowercase())
        binding.taskDetailsTaskTypeName.text = tarea.typeTask.toString().replaceRange(1, tarea.typeTask.toString().length, tarea.typeTask.toString().substring(1).lowercase())
        binding.taskDetailsDateCreation.text = tarea.fechCreation.toString().substring(0, tarea.fechCreation.toString().lastIndexOf("T"))
        binding.taskDetailsDateEnd.text = tarea.fechFinalization.toString().substring(0, tarea.fechFinalization.toString().lastIndexOf("T"))
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

    @SuppressLint("NotifyDataSetChanged")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.task_details_button_delete -> {
                deleteConfirmation()
                true
            }

            R.id.task_details_button_edit -> {
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun deleteConfirmation() {

        findNavController().navigate(
            TaskListDirections.actionTaskListToBaseFragmentDialog2(
                getString(com.moronlu18.invoice.R.string.title_fragmentDialogExit),
                getString(R.string.delete_task_info)
            )
        )
        parentFragmentManager.setFragmentResultListener(
            BaseFragmentDialog.request,
            viewLifecycleOwner
        ) { _, result ->
            val success = result.getBoolean(BaseFragmentDialog.result, false)
            if (success) {
                val position = TaskProvider.taskDataSet.indexOf(args.task)

                if (position != -1) {
                    TaskProvider.taskDataSet.removeAt(position)
                    adapter.notifyItemRemoved(position)

                    Handler(Looper.getMainLooper()).postDelayed({
                        findNavController().popBackStack()
                    }, 100)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}