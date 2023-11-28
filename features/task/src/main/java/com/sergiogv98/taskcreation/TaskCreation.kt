package com.sergiogv98.taskcreation

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.moronlu18.accounts.entity.Task
import com.moronlu18.accounts.enum.TaskStatus
import com.moronlu18.accounts.enum.TypeTask
import com.moronlu18.accounts.repository.TaskProvider
import com.moronlu18.tasklist.R
import com.moronlu18.tasklist.databinding.FragmentTaskCreationBinding
import com.sergiogv98.usecase.TaskState
import com.sergiogv98.usecase.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private val calendar = Calendar.getInstance()

class TaskCreation : Fragment() {

    private var _binding: FragmentTaskCreationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTaskCreationBinding.inflate(inflater, container, false)
        binding.viewmodeltaskcreation = this.viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.taskCreationButtonDateCreation.setOnClickListener {
            showDatePicker(binding.taskCreationButtonDateCreation)
        }

        binding.taskCreationButtonDateEnd.setOnClickListener {
            showDatePicker(binding.taskCreationButtonDateEnd)
        }

        binding.taskCreationButtonAdd.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.popBackStack()
        }

        viewModel.getState().observe(viewLifecycleOwner) {
            when (it) {
                TaskState.OnSuccess -> onSuccessCreate()
                else -> {}
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showDatePicker(button: Button) {
        val datePickerDialog = DatePickerDialog(
            requireContext(), { DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)
                button.text = formattedDate
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)

        )
        datePickerDialog.show();

    }

    private fun onSuccessCreate() {
        val nameClient = binding.taskCreationInfoTxvCustomer.text.toString()
        val nameTask = binding.taskCreationTxvTaskName.text.toString()
        val description = binding.taskCreationTxvDescription.text.toString()
        val fechaCreation = binding.taskCreationButtonDateCreation.text.toString()
        val fechaEnd = binding.taskCreationButtonDateEnd.text.toString()

        val task = Task(
            id = (TaskProvider.taskDataSet.size + 1).coerceAtLeast(1),
            nomClient = nameClient,
            nomTask = nameTask,
            typeTask = taskTypeChoose(),
            taskStatus = taskStatusChoose(),
            descTask = description,
            fechCreation = fechaCreation,
            fechFinalization = fechaEnd
        )
        TaskProvider.taskDataSet.add(task)

        findNavController().popBackStack()
    }

    private fun taskTypeChoose(): TypeTask {
        return when (binding.taskCreationTypeTaskList.selectedItemId) {
            0L -> TypeTask.PRIVADA
            1L -> TypeTask.LLAMAR
            2L -> TypeTask.VISITA
            else -> {
                TypeTask.PRIVADA
            }
        }
    }

    private fun taskStatusChoose(): TaskStatus {
        return when (binding.taskCreationStatus.checkedRadioButtonId) {
            binding.taskCreationRdbPendiente.id -> TaskStatus.PENDIENTE
            binding.taskCreationRdbModificada.id -> TaskStatus.MODIFICADA
            binding.taskCreationRdbVencida.id -> TaskStatus.VENCIDA
            binding.taskCreationRdbFinalizada.id -> TaskStatus.FINALIZADA
            else -> TaskStatus.FINALIZADA
        }
    }
}