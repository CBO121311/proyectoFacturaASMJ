package com.sergiogv98.tasklist.ui

import com.moronlu18.accounts.entity.Task
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.moronlu18.invoice.base.BaseFragmentDialog
import com.moronlu18.tasklist.R
import com.sergiogv98.tasklist.adapter.TaskAdapter
import com.moronlu18.tasklist.databinding.FragmentTaskListBinding
import com.sergiogv98.usecase.TaskListViewModel


class TaskList : Fragment() {


    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TaskListViewModel by viewModels()
    private lateinit var adapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)

        binding.viewmodel = this.viewModel
        binding.lifecycleOwner = this
        binding.taskListAddTask.setOnClickListener {
            findNavController().navigate(R.id.action_taskList_to_taskCreation)
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerViewTask()

        viewModel.getTaskList()
        viewModel.getState().observe(viewLifecycleOwner) {
            when (it) {
                TaskListState.NoData -> showNoData()
                is TaskListState.Success -> onSuccess(it.dataset)
            }
        }
    }

    private fun initRecyclerViewTask() {

        adapter = TaskAdapter(
            taskList = viewModel.getTasks(),
            onClickListener = { task -> onItemSelected(task) },
            onClickDeleted = { position -> onDeletedItem(position) },
            onClickEdit = { position -> onEditItem(position) }
        )

        binding.taskListRecyclerTasks.layoutManager = LinearLayoutManager(requireContext())
        binding.taskListRecyclerTasks.adapter = adapter
    }

    /**
     * Envía un objeto (Task) al layout taskDetail utilizando SafeArgs
     */
    private fun onItemSelected(task: Task) {
        findNavController().navigate(TaskListDirections.actionTaskListToTaskDetail(task))
    }

    private fun onEditItem(position: Int){
        val bundle = Bundle()
        bundle.putInt("taskPosition", position)
        parentFragmentManager.setFragmentResult("taskkey", bundle)
        findNavController().navigate(R.id.action_taskList_to_taskCreation)
    }

    private fun onDeletedItem(position: Int) {

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
                viewModel.deleteTask(position)
                adapter.notifyItemRemoved(position)
                if(viewModel.getTasks().isEmpty()) {
                    showNoData()
                }
            }
        }
    }

    private fun showNoData() {
        binding.taskListRecyclerTasks.visibility = View.GONE
        binding.taskListLlEmpty.visibility = View.VISIBLE
        binding.taskListLlEmptyImg.playAnimation()
    }

    private fun onSuccess(dataset: ArrayList<Task>) {
        binding.taskListRecyclerTasks.visibility = View.VISIBLE
        binding.taskListLlEmpty.visibility = View.GONE
        binding.taskListLlEmptyImg.cancelAnimation()
        adapter.update(dataset)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}