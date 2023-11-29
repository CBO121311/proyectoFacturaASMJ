package com.sergiogv98.tasklist.ui

import com.moronlu18.accounts.entity.Task
import com.moronlu18.accounts.repository.TaskProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.moronlu18.invoice.base.BaseFragmentDialog
import com.moronlu18.tasklist.R
import com.sergiogv98.tasklist.adapter.TaskAdapter
import com.moronlu18.tasklist.databinding.FragmentTaskListBinding


class TaskList : Fragment() {


    private var _binding:FragmentTaskListBinding? = null
    private val binding get() = _binding!!
    private var taskMutableList: MutableList<Task> = TaskProvider.taskDataSet
    private lateinit var adapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskListBinding.inflate(inflater,container,false)

        binding.taskListAddTask.setOnClickListener {
            findNavController().navigate(R.id.action_taskList_to_taskCreation)
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerViewTask()
    }

    private fun initRecyclerViewTask(){

        adapter = TaskAdapter(
            taskList = taskMutableList,
            onClickListener = { task ->  onItemSelected(task)},
            onClickDeleted = { position -> onDeletedItem(position)}
        )

        updateEmptyView()

        binding.taskListRecyclerTasks.layoutManager = LinearLayoutManager(requireContext())
        binding.taskListRecyclerTasks.adapter = adapter
    }

    /**
     * Envía un objeto (Task) al layout taskDetail utilizando SafeArgs
     */
    fun onItemSelected(task: Task) {
        findNavController().navigate(TaskListDirections.actionTaskListToTaskDetail(task))
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
                taskMutableList.removeAt(position)
                adapter.notifyItemRemoved(position)
                updateEmptyView()
            }
        }
    }

    private fun updateEmptyView(){
        if(taskMutableList.isEmpty()){
            binding.taskListLlEmpty.visibility = View.VISIBLE
        } else {
            binding.taskListLlEmpty.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}