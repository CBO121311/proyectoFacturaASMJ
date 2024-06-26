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
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.moronlu18.data.task.Task
import com.moronlu18.data.task.TaskStatus
import com.moronlu18.data.task.TypeTask
import com.moronlu18.invoice.ui.MainActivity
import com.moronlu18.invoice.utils.createNotificationChannel
import com.moronlu18.invoice.utils.sendNotification
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
    private val args: TaskCreationArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskCreationBinding.inflate(inflater, container, false)
        binding.viewmodeltaskcreation = this.viewModel
        binding.lifecycleOwner = this
        viewModel.setEditorMode(false)
        createNotificationChannel(CHANNEL_ID, requireContext())

        binding.autoCompleteTxt.addTextChangedListener(GeneralTextWatcher(binding.taskCreationTaskDropdown))
        binding.taskCreationTxvTaskName.addTextChangedListener(GeneralTextWatcher(binding.taskCreationTaskTilName))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpFab()

        binding.taskCreationImgCalendarCreation.setOnClickListener {
            showDatePicker(binding.taskCreationDateCreationTxtShow)
        }

        binding.taskCreationBtnCalendarEnd.setOnClickListener {
            showDatePicker(binding.taskCreationDateEndTxtShow)
        }

        binding.taskCreationButtonAdd.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.popBackStack()
        }

        viewModel.getState().observe(viewLifecycleOwner) {
            when (it) {
                TaskCreationState.TitleIsMandatory -> setTaskNameEmptyError()
                TaskCreationState.CustomerUnspecified -> setTaskCustomerEmptyError()
                TaskCreationState.IncorrectDateRange -> setDateValidationError()
                else -> onSuccessCreate()
            }
        }

        val task: Task? = args.tasknav

        if(task != null){
            setTaskContent(task)
            clientDropDownInit()
        } else {
            clientDropDownInit()
        }

    }

    private fun setTaskContent(task: Task) {
        viewModel.taskName.value = task.nomTask
        viewModel.dateCreation.value = processDateString(task.dateCreation)
        viewModel.dateEnd.value = processDateString(task.dateFinalization)
        binding.taskCreationTxvDescription.setText(task.descTask)
        binding.taskCreationTypeTaskList.setSelection(returnTaskType(task))
        setTaskStatusInRadioGroup(task)
    }

    private fun showDatePicker(text: TextView) {
        val datePickerDialog = DatePickerDialog(
            requireContext(), { _, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)
                text.text = formattedDate
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)

        )
        datePickerDialog.show();

    }

    private fun onSuccessCreate() {
        val task: Task? = args.tasknav
        val selectedClientName = binding.autoCompleteTxt.text.toString()
        val selectedClientId = viewModel.getCustomerId(selectedClientName)

        if (task != null) {
            task.nomTask = binding.taskCreationTxvTaskName.text.toString()
            task.customerId = selectedClientId
            task.descTask = binding.taskCreationTxvDescription.text.toString()
            task.taskStatus = taskStatusChoose()
            task.typeTask = taskTypeChoose()
            task.dateCreation = processDate(binding.taskCreationDateCreationTxtShow.text.toString())
            task.dateFinalization = processDate(binding.taskCreationDateEndTxtShow.text.toString())

            sendNotification(CHANNEL_ID, requireContext(), getString(com.moronlu18.tasklist.R.string.NotificationTaskEdit), getString(com.moronlu18.tasklist.R.string.NotificationTextContextTask) + " " + (task.nomTask))
            viewModel.updateTask(task)
        } else {
            val id = viewModel.getNextTaskId()
            val newTask = Task.create(
                id = id,
                customerId = selectedClientId,
                nameTask = binding.taskCreationTxvTaskName.text.toString(),
                descTask = binding.taskCreationTxvDescription.text.toString(),
                taskStatus = taskStatusChoose(),
                typeTask = taskTypeChoose(),
                dateCreation = processDate(binding.taskCreationDateCreationTxtShow.text.toString()),
                dateFinalization = processDate(binding.taskCreationDateEndTxtShow.text.toString())
            )

            sendNotification(CHANNEL_ID, requireContext(), getString(com.moronlu18.tasklist.R.string.NotificationTaskCreate), getString(com.moronlu18.tasklist.R.string.NotificationTextContextTask) + " " + (newTask.nomTask))

            viewModel.addTaskRepository(newTask)
        }
        findNavController().popBackStack()
    }

    private fun clientDropDownInit() {
        val task: Task? = args.tasknav
        val customerList = viewModel.giveListCustomerName()

        if (task!= null){

            binding.autoCompleteTxt.setText(viewModel.getCustomerName(task.customerId.value))
            viewModel.customerName.value = viewModel.getCustomerName(task.customerId.value)
        }
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_dropdown_item_1line, customerList)
        binding.autoCompleteTxt.setAdapter(adapter)
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

    private fun setUpFab() {
        (requireActivity() as? MainActivity)?.fab?.apply {
            visibility = View.INVISIBLE
        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val CHANNEL_ID = "TaskCreation_Notifications"
    }
}