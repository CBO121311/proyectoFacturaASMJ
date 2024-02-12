package com.sergiogv98.tasklist.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.moronlu18.data.task.Task
import com.moronlu18.invoice.base.BaseFragmentDialog
import com.moronlu18.invoice.ui.MainActivity
import com.moronlu18.tasklist.R
import com.moronlu18.tasklist.databinding.FragmentTaskDetailBinding
import com.sergiogv98.usecase.TaskDetailViewModel
import com.sergiogv98.utils.TaskUtils

class TaskDetail : Fragment() {

    private val viewModel: TaskDetailViewModel by viewModels()
    private var _binding: FragmentTaskDetailBinding? = null
    private val binding get() = _binding!!
    private val args: TaskDetailArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskDetailBinding.inflate(inflater, container, false)
        binding.viewmodeltaskdetail = this.viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpFab()

        viewModel.getState().observe(viewLifecycleOwner){
            when(it){
                TaskDetailState.OnSuccess -> onSuccess()
            }
        }

        onSuccess()

        binding.taskDetailsButtonEdit.setOnClickListener {
            onEditItem(args.tasknav)
        }

        binding.taskDetailsButtonDelete.setOnClickListener {
            deleteTaskConfirmation()
        }

        binding.taskDetailsButtonBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun onSuccess(){
        val task = args.tasknav
        viewModel.let {
            it.customerName.value = viewModel.getCustomer(task.customerId)!!.name
            it.taskName.value = task.nomTask
            it.taskStatus.value = TaskUtils().taskStatusGive( task.dateFinalization, task.taskStatus)
            it.typeTask.value = TaskUtils().formatType(task.typeTask)
            it.dateCreation.value = TaskUtils().convertInstantToDateText(task.dateCreation)
            it.dateEnd.value = TaskUtils().convertInstantToDateText(task.dateFinalization)
            it.taskDescription.value = task.descTask

        }

        val customer = viewModel.getCustomer(task.customerId)

        if (customer?.photo!=null){
            binding.taskDetailsClientImageView.setImageURI(customer?.photo)
        }else{
            binding.taskDetailsClientImageView.setImageResource(R.drawable.kiwidinero)
        }
    }

    private fun onEditItem(task: Task){
        findNavController().navigate(TaskDetailDirections.actionTaskDetailToTaskCreation(task))
    }


    private fun deleteTaskConfirmation(){
        val task = args.tasknav

        val dialog = BaseFragmentDialog.newInstance(
            getString(R.string.delete_task_info_general),
            getString(R.string.delete_task_info)
        )

        dialog.show(childFragmentManager, "delete_dialog")

        dialog.parentFragmentManager.setFragmentResultListener(
            BaseFragmentDialog.request,
            viewLifecycleOwner
        ) { _, result ->
            if(!result.getBoolean(BaseFragmentDialog.request)){
                viewModel.delete(task)
                findNavController().popBackStack()
            }
        }
    }

    private fun setUpFab() {
        (requireActivity() as? MainActivity)?.fab?.apply {
            visibility = View.GONE
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.onSuccess()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}