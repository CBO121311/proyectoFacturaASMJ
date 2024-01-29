package com.sergiogv98.tasklist.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.moronlu18.data.task.Task
import com.moronlu18.invoice.ui.MainActivity
import com.moronlu18.tasklist.R
import com.moronlu18.tasklist.databinding.FragmentTaskDetailBinding
import com.sergiogv98.usecase.TaskDetailViewModel

class TaskDetail : Fragment() {

    private val viewModel: TaskDetailViewModel by viewModels()
    private var _binding: FragmentTaskDetailBinding? = null
    private val binding get() = _binding!!
    private var task: Task? = null
    private var posTask: Int = 0

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

        if(task == null){
            parentFragmentManager.setFragmentResultListener("detailkey", this,
                FragmentResultListener{ _, result ->
                    posTask = result.getInt("detailposition")
                    task = viewModel.getTaskByPosition(posTask)
                }
            )
        }

        binding.taskDetailsButtonEdit.setOnClickListener {
            onEditItem(task!!)
        }

        binding.taskDetailsButtonBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun onSuccess(){
        viewModel.let {
            it.customerName.value = viewModel.getCustomerName(task?.customerId?.value ?: 0)
            it.taskName.value = task?.nomTask
            it.taskStatus.value = task?.taskStatus.toString()
            it.typeTask.value = task?.typeTask.toString()
            it.dateCreation.value = task?.dateCreation.toString()
            it.dateEnd.value = task?.dateFinalization.toString()
            it.taskDescription.value = task?.descTask
        }
        val customer = viewModel.getCustomerPhoto(task!!.customerId.value)

        if (customer.phototrial != null) {
            binding.taskDetailsClientImageView.setImageResource(customer.phototrial!!)
        } else {
            binding.taskDetailsClientImageView.setImageBitmap(customer.photo)
        }
    }

    private fun onEditItem(task: Task){
        val posTask = viewModel.getPositionByTask(task)
        val bundle = Bundle()
        bundle.putInt("taskposition", posTask)
        Log.d("taskkey", posTask.toString())
        parentFragmentManager.setFragmentResult("taskkey", bundle)
        findNavController().navigate(R.id.action_taskDetail_to_taskCreation)
    }

    private fun setUpFab() {
        (requireActivity() as? MainActivity)?.fab?.apply {
            visibility = View.GONE
        }
    }

    override fun onStart() {
        super.onStart()
        task = viewModel.getTaskByPosition(posTask)
        viewModel.onSuccess()
    }

    override fun onResume() {
        super.onResume()
        task = viewModel.getTaskByPosition(posTask)
        viewModel.onSuccess()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    /*
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

    }*/
}