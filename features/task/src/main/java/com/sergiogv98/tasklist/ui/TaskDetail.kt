package com.sergiogv98.tasklist.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.moronlu18.data.customer.Customer
import com.moronlu18.data.task.Task
import com.moronlu18.invoice.ui.MainActivity
import com.moronlu18.tasklist.R
import com.moronlu18.tasklist.databinding.FragmentTaskDetailBinding
import com.sergiogv98.tasklist.adapter.TaskAdapter
import com.sergiogv98.usecase.TaskDetailViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


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
        setUpFab()
        var taskMutableList: MutableList<Task> = viewModel.getTaskDataSet()

        adapter = TaskAdapter(
            taskList = taskMutableList,
        )

        var task: Task = args.task


        //TODO Cambiado por mi por el tema de la foto
        val customer = viewModel.getCustomerPhoto(task.customerId.value as Int)

        if (customer.phototrial != null) {
            binding.taskDetailsClientImageView.setImageResource(customer.phototrial!!)
        } else {
            binding.taskDetailsClientImageView.setImageBitmap(customer.photo)
        }

        binding.taskDetailsClientNameTxt.text = customer.name
        binding.taskDetailsTaskName.text = task.nomTask
        val currentDate = Date()
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

        val taskEndDate = sdf.parse(task.dateFinalization.toString())

        if (currentDate.after(taskEndDate)) {
            binding.taskDetailsStatusButton.text = getString(R.string.task_vencida)
        } else {
            binding.taskDetailsStatusButton.text = task.taskStatus.toString().replaceRange(1, task.taskStatus.toString().length, task.taskStatus.toString().substring(1).lowercase())
        }

        binding.taskDetailsTaskTypeName.text = task.typeTask.toString().replaceRange(1, task.typeTask.toString().length, task.typeTask.toString().substring(1).lowercase())
        binding.taskDetailsDateCreation.text = task.dateCreation.toString().substring(0, task.dateCreation.toString().lastIndexOf("T"))
        binding.taskDetailsDateEnd.text = task.dateFinalization.toString().substring(0, task.dateFinalization.toString().lastIndexOf("T"))
        binding.taskDetailsDescription.text = task.descTask

        binding.taskDetailsButtonEdit.setOnClickListener {
            onEditItem(task)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.taskDetailsButtonBack.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.popBackStack()
        }

    }

    private fun setUpFab() {
        (requireActivity() as? MainActivity)?.fab?.apply {
            visibility = View.GONE
        }
    }

    private fun onEditItem(task: Task) {
        val posTask = viewModel.getPositionByTask(task)
        val bundle = Bundle();
        bundle.putInt("taskPositionEdit", posTask)

        parentFragmentManager.setFragmentResult("taskKeyEdit", bundle)
        findNavController().navigate(R.id.action_taskDetail_to_taskCreation)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()

    }
}