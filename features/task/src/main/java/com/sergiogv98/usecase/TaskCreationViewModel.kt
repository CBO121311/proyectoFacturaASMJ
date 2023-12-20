package com.sergiogv98.usecase

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moronlu18.accounts.entity.Customer
import com.moronlu18.accounts.entity.Task
import com.moronlu18.accounts.repository.CustomerProvider
import com.moronlu18.accounts.repository.TaskProvider
import com.sergiogv98.tasklist.ui.TaskState
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class TaskCreationViewModel : ViewModel() {

    var taskName = MutableLiveData<String>()
    var customerName = MutableLiveData<String>()
    var dateCreation = MutableLiveData<String>()
    var dateEnd = MutableLiveData<String>()
    var prevTask: Task? = null

    private var state = MutableLiveData<TaskState>()
    private var isEditor = MutableLiveData<Boolean>()

    fun validateTask() {
        when {
            TextUtils.isEmpty(customerName.value) -> state.value = TaskState.CustomerUnspecified
            TextUtils.isEmpty(taskName.value) -> state.value = TaskState.TitleIsMandatory
            isValidDate(dateCreation.value, dateEnd.value) -> state.value =
                TaskState.IncorrectDateRange

            else -> {
                state.value = TaskState.OnSuccess
            }
        }
    }


    fun getState(): LiveData<TaskState> {
        return state
    }

    fun taskGiveId(): Int {
        return (TaskProvider.taskDataSet.size + 1).coerceAtLeast(1)
    }

    fun taskGiveCustomerId(nameCustomer: String): Customer? {
        return CustomerProvider.getListCustomer().find { it.name == nameCustomer }
    }

    fun taskGive(position: Int): Task{
        return TaskProvider.taskDataSet[position]
    }

    fun giveListCustomer() : List<String> {
        return CustomerProvider.getListCustomer().map { it.name }
    }

    fun giveClientName(id: Int): String{
        return CustomerProvider.getCustomerNameById(id).toString()
    }

    fun addTaskRepository(task: Task){
        TaskProvider.taskDataSet.add(task)
    }

    fun updateTask(task: Task, position: Int){
        TaskProvider.updateTasks(task, position)
    }

    fun setEditorMode(editorMode: Boolean){
        isEditor.value = editorMode
    }

    fun getEditorMode(): Boolean {
        return isEditor.value?: false
    }

    private fun isValidDate(fechCrea: String?, fechEnd: String?): Boolean {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        lateinit var fechaCreation: Instant
        lateinit var fechaEnd: Instant
        try {
            fechaCreation = fechCrea?.let {
                LocalDate.parse(it, formatter)
                    .atStartOfDay(ZoneOffset.UTC)
                    .toInstant()
            } ?: Instant.now()

            fechaEnd = LocalDate.parse(fechEnd, formatter)
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant()
        } catch (e: Exception) {
            return false
        }

        return fechaEnd.isBefore(fechaCreation)
    }


}