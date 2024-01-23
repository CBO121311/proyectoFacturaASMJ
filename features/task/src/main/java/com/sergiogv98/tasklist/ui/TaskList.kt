package com.sergiogv98.tasklist.ui

import com.moronlu18.data.task.Task
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.moronlu18.invoice.base.BaseFragmentDialog
import com.moronlu18.invoice.ui.MainActivity
import com.moronlu18.tasklist.R
import com.sergiogv98.tasklist.adapter.TaskAdapter
import com.moronlu18.tasklist.databinding.FragmentTaskListBinding
import com.sergiogv98.usecase.TaskListViewModel


class TaskList : Fragment(), MenuProvider {


    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TaskListViewModel by viewModels()
    private lateinit var adapter: TaskAdapter
    private var firstCharge = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        binding.viewmodel = this.viewModel
        binding.lifecycleOwner = this
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolBar()
        initRecyclerViewTask()
        if (firstCharge) {
            viewModel.getTaskList(firstCharge)
            firstCharge = false
        } else {
            viewModel.getTaskList(firstCharge)
        }
        setUpFab(false)
        viewModel.getState().observe(viewLifecycleOwner) {
            when (it) {
                is TaskListState.Loading -> showProgressBar(it.value)
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

    private fun onEditItem(position: Int) {
        val bundle = Bundle()
        bundle.putInt("taskPosition", position)
        parentFragmentManager.setFragmentResult("taskkey", bundle)
        findNavController().navigate(R.id.action_taskList_to_taskCreation)
    }

    private fun onDeletedItem(position: Int) {

        findNavController().navigate(
            TaskListDirections.actionTaskListToBaseFragmentDialog2(
                getString(R.string.delete_task_info_general),
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
                if (viewModel.getTasks().isEmpty()) {
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

    private fun showNothing() {
        binding.taskListRecyclerTasks.visibility = View.GONE
        binding.taskListLlEmpty.visibility = View.GONE
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

    private fun showProgressBar(value: Boolean) {

        if (value) {
            showNothing()
            setUpFab(true)
            findNavController().navigate(R.id.action_taskList_to_fragmentProgressDialogKiwi)
        } else {
            setUpFab(false)
            findNavController().popBackStack()
        }
    }

    private fun setUpFab(charge: Boolean) {
        (requireActivity() as? MainActivity)?.fab?.apply {
            visibility = if(charge){
                View.GONE
            } else {
                View.VISIBLE
            }
            setImageResource(com.moronlu18.invoice.R.drawable.ic_action_add)
            setOnClickListener {
                findNavController().navigate(R.id.action_taskList_to_taskCreation)
            }
        }
    }

    private fun setUpToolBar() {
        (requireActivity() as? MainActivity)?.toolbar?.apply {
            visibility = View.VISIBLE
        }

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_task_list, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.menu_action_refresh -> {
                viewModel.getTaskList(true)
                true
            }

            R.id.menu_action_sort -> {
                adapter.toggleSortOrder()
                true
            }

            else -> false
        }
    }
}