package com.sergiogv98.tasklist.ui

import com.moronlu18.data.task.Task
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.moronlu18.invoice.base.BaseFragmentDialog
import com.moronlu18.invoice.ui.MainActivity
import com.moronlu18.tasklist.R
import com.sergiogv98.tasklist.adapter.TaskAdapter
import com.moronlu18.tasklist.databinding.FragmentTaskListBinding
import com.sergiogv98.usecase.TaskListViewModel


class TaskList : Fragment(), MenuProvider, TaskAdapter.OnTaskClick {


    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TaskListViewModel by viewModels()
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        binding.viewmodel = this.viewModel
        binding.lifecycleOwner = this
        showNothing()

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolBar()
        initRecyclerViewTask()
        setUpFab()

        var appBarConfiguration =
            AppBarConfiguration.Builder(R.id.taskList)
                .setOpenableLayout((requireActivity() as MainActivity).drawer)
                .build()

        NavigationUI.setupWithNavController(
            (requireActivity() as MainActivity).toolbar,
            findNavController(),
            appBarConfiguration
        )


        viewModel.getState().observe(viewLifecycleOwner) {
            when (it) {
                is TaskListState.Loading -> showProgressBar(it.value)
                TaskListState.NoData -> showNoData()
                is TaskListState.Success -> onSuccess()
            }
        }

        viewModel.allTask.observe(viewLifecycleOwner){
            it.let { taskAdapter.submitList(it) }
        }
    }

    private fun initRecyclerViewTask() {

        taskAdapter = TaskAdapter(this)

        binding.taskListRecyclerTasks.layoutManager = LinearLayoutManager(requireContext())
        binding.taskListRecyclerTasks.adapter = taskAdapter
    }

    override fun taskClick(task: Task) {
        navigateDetailTask(task)
    }

    override fun taskOnLongClick(view: View, task: Task) {
        showPopUpMenu(view, task)
    }

    private fun onSuccess() {
        binding.taskListRecyclerTasks.visibility = View.VISIBLE
        binding.taskListLlEmpty.visibility = View.GONE
        binding.taskListLlEmptyImg.cancelAnimation()
        //taskAdapter.update(dataset)
    }

    private fun navigateDetailTask(task: Task){
        findNavController().navigate(
            TaskListDirections.actionTaskListToTaskDetail(task)
        )
    }

    private fun onEditItem(task: Task){
        findNavController().navigate(TaskListDirections.actionTaskListToTaskCreation(task))
    }

    private fun onDeletedItem(task: Task) {

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
                viewModel.deleteTask(task)
            }
        }
    }

    private fun showPopUpMenu(view: View, task: Task){
        val popupMenu = PopupMenu(requireContext(), view, Gravity.END)

        popupMenu.inflate(R.menu.menu_pop)

        popupMenu.setOnMenuItemClickListener {

            when(it.itemId){

                R.id.menu_see -> {
                    navigateDetailTask(task)
                    true
                }

                R.id.menu_edit -> {
                    onEditItem(task)
                    true
                }

                R.id.menu_delete -> {
                    onDeletedItem(task)
                    true
                }

                else -> false
            }
        }

        popupMenu.setOnDismissListener {
            taskAdapter.clearSelection()
        }

        popupMenu.show()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_task_list, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.menu_action_refresh -> {
                viewModel.getTaskList()
                taskAdapter.sortId()
                true
            }

            R.id.menu_action_sort -> {
                taskAdapter.toggleSortOrder()
                true
            }

            else -> false
        }
    }

    private fun setUpToolBar() {
        (requireActivity() as? MainActivity)?.toolbar?.apply {
            visibility = View.VISIBLE
        }

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setUpFab() {
        (requireActivity() as? MainActivity)?.fab?.apply {
            visibility = View.VISIBLE
            setImageResource(com.moronlu18.invoice.R.drawable.ic_action_add)
            setOnClickListener {
                findNavController().navigate(R.id.action_taskList_to_taskCreation)
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

    private fun showProgressBar(value: Boolean) {

        if (value) {
            findNavController().navigate(R.id.action_taskList_to_fragmentProgressDialogKiwi)
        } else {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        viewModel.getTaskList()
    }

}