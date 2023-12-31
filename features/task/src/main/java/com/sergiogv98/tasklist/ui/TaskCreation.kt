package com.sergiogv98.tasklist.ui

import android.R
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.moronlu18.accounts.entity.Task
import com.moronlu18.accounts.enum.TaskStatus
import com.moronlu18.accounts.enum.TypeTask
import com.moronlu18.tasklist.databinding.FragmentTaskCreationBinding
import com.sergiogv98.usecase.TaskCreationViewModel
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TaskCreation : Fragment() {

    private var _binding: FragmentTaskCreationBinding? = null
    private val binding get() = _binding!!
    private val calendar = Calendar.getInstance()
    private val viewModel: TaskCreationViewModel by viewModels()
    private var editTaskPos = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskCreationBinding.inflate(inflater, container, false)
        binding.viewmodeltaskcreation = this.viewModel
        binding.lifecycleOwner = this
        viewModel.setEditorMode(false)

        parentFragmentManager.setFragmentResultListener(
            "taskkey", this
        ) { _, result ->
            var posTask: Int = result.getInt("taskPosition")
            val taskEdit = viewModel.taskGive(posTask)
            viewModel.setEditorMode(true)



            binding.autoCompleteTxt.setText(viewModel.giveClientName(taskEdit.clientID.id))
            binding.taskCreationTxvTaskName.setText(taskEdit.nomTask)
            binding.taskCreationButtonDateCreation.text = processDateString(taskEdit.fechCreation)
            binding.taskCreationButtonDateEnd.text = processDateString(taskEdit.fechFinalization)
            binding.taskCreationTxvDescription.setText(taskEdit.descTask)
            binding.taskCreationTypeTaskList.setSelection(returnTaskType(taskEdit))
            setTaskStatusInRadioGroup(taskEdit)

            clientDropDownInit()
            editTaskPos = posTask
            viewModel.prevTask = taskEdit
        }


        clientDropDownInit()

        binding.autoCompleteTxt.addTextChangedListener(GeneralTextWatcher(binding.taskCreationTaskDropdown))
        binding.taskCreationTxvTaskName.addTextChangedListener(GeneralTextWatcher(binding.taskCreationTaskTilName))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.taskCreationImgCalendarCreation.setOnClickListener {
            showDatePicker(binding.taskCreationButtonDateCreation)
        }

        binding.taskCreationImgCalendarEnd.setOnClickListener {
            showDatePicker(binding.taskCreationButtonDateEnd)
        }

        binding.taskCreationButtonAdd.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.popBackStack()
        }

        viewModel.getState().observe(viewLifecycleOwner) {
            when (it) {
                TaskState.TitleIsMandatory -> setTaskNameEmptyError()
                TaskState.CustomerUnspecified -> setTaskCustomerEmptyError()
                TaskState.IncorrectDateRange -> setDateValidationError()
                else -> onSuccessCreate()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showDatePicker(button: Button) {
        val datePickerDialog = DatePickerDialog(
            requireContext(), { _, year: Int, monthOfYear: Int, dayOfMonth: Int ->
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

    private fun clientDropDownInit(){
        binding.autoCompleteTxt.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.simple_dropdown_item_1line,
                viewModel.giveListCustomer()
            )
        )
    }

    private fun onSuccessCreate() {
        val selectedClientName = binding.autoCompleteTxt.text.toString()
        val selectedClient = viewModel.taskGiveCustomerId(selectedClientName)
        val nameTask = binding.taskCreationTxvTaskName.text.toString()
        val description = binding.taskCreationTxvDescription.text.toString()
        val fechaCreation = processDate(binding.taskCreationButtonDateCreation.text.toString())
        val fechaEnd = processDate(binding.taskCreationButtonDateEnd.text.toString())

        if (viewModel.getEditorMode()) {
            val updateTask = Task(
                id = editTaskPos,
                clientID = selectedClient!!,
                nomTask = nameTask,
                typeTask = taskTypeChoose(),
                taskStatus = taskStatusChoose(),
                descTask = description,
                fechCreation = fechaCreation,
                fechFinalization = fechaEnd
            )
            viewModel.updateTask(updateTask, editTaskPos)
        } else {
            val task = Task(
                id = viewModel.taskGiveId(),
                clientID = selectedClient!!,
                nomTask = nameTask,
                typeTask = taskTypeChoose(),
                taskStatus = taskStatusChoose(),
                descTask = description,
                fechCreation = fechaCreation,
                fechFinalization = fechaEnd
            )
            viewModel.addTaskRepository(task)
        }
        findNavController().popBackStack()
    }

    private fun processDate(dateString: String): Instant {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        return try {
            val localDate = LocalDate.parse(dateString, formatter)
            localDate.atStartOfDay(ZoneOffset.UTC).toInstant()
        } catch (e: Exception) {
            Instant.now()
        }
    }

    private fun processDateString(date: Instant): String {
        val inputDate = Date.from(date)
        val selectedDate = Calendar.getInstance()
        selectedDate.time = inputDate
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(selectedDate.time)
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

    private fun returnTaskType(task: Task): Int {
        return when(task.typeTask){
            TypeTask.PRIVADA -> 0
            TypeTask.LLAMAR -> 1
            TypeTask.VISITA -> 2
        }
    }

    private fun taskStatusChoose(): TaskStatus {
        return when (binding.taskCreationStatus.checkedRadioButtonId) {
            binding.taskCreationRdbPendiente.id -> TaskStatus.PENDIENTE
            binding.taskCreationRdbModificada.id -> TaskStatus.MODIFICADA
            binding.taskCreationRdbVencida.id -> TaskStatus.VENCIDA
            binding.taskCreationRdbFinalizada.id -> TaskStatus.FINALIZADA
            else -> TaskStatus.PENDIENTE
        }
    }

    private fun setTaskStatusInRadioGroup(task: Task) {
        val statusRadioGroup = binding.taskCreationStatus

        val radioButtonId = when (task.taskStatus) {
            TaskStatus.PENDIENTE -> binding.taskCreationRdbPendiente.id
            TaskStatus.MODIFICADA -> binding.taskCreationRdbModificada.id
            TaskStatus.VENCIDA -> binding.taskCreationRdbVencida.id
            TaskStatus.FINALIZADA -> binding.taskCreationRdbFinalizada.id
        }
        statusRadioGroup.check(radioButtonId)
    }


    private fun setTaskNameEmptyError() {
        binding.taskCreationTaskTilName.error =
            getString(com.moronlu18.tasklist.R.string.name_error)
        binding.taskCreationTaskTilName.requestFocus()
    }

    private fun setTaskCustomerEmptyError() {
        binding.taskCreationTaskDropdown.error =
            getString(com.moronlu18.tasklist.R.string.client_error)
        binding.taskCreationTaskDropdown.requestFocus()
    }

    private fun setDateValidationError() {
        Snackbar.make(
            binding.taskCreationView,
            getString(com.moronlu18.tasklist.R.string.date_error),
            Snackbar.ANIMATION_MODE_SLIDE
        ).show()
    }

    inner class GeneralTextWatcher(private val til: TextInputLayout) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            til.error = null
        }

    }
}