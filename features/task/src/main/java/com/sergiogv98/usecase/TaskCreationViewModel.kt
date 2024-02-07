package com.sergiogv98.usecase

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moronlu18.data.base.CustomerId
import com.moronlu18.data.task.Task
import com.moronlu18.repository.TaskRepositoryBD
import com.sergiogv98.tasklist.ui.TaskCreationState
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class TaskCreationViewModel : ViewModel() {

    var taskName = MutableLiveData<String>()
    var customerName = MutableLiveData<String>()
    private var dateCreation = MutableLiveData<String>()
    private var dateEnd = MutableLiveData<String>()

    private var taskRepositoryBD = TaskRepositoryBD()

    private var state = MutableLiveData<TaskCreationState>()
    private var isEditor = MutableLiveData<Boolean>()

    fun validateTask() {
        when {
            TextUtils.isEmpty(customerName.value) -> state.value = TaskCreationState.CustomerUnspecified
            TextUtils.isEmpty(taskName.value) -> state.value = TaskCreationState.TitleIsMandatory
            isValidDate(dateCreation.value, dateEnd.value) -> state.value =
                TaskCreationState.IncorrectDateRange

            else -> {
                state.value = TaskCreationState.OnSuccess
            }
        }
    }


    fun getState(): LiveData<TaskCreationState> {
        return state
    }

    fun getNextTaskId(): Int {
        return 1 + (taskRepositoryBD.getLastTaskId() ?: 0)
    }

    fun giveListCustomer(): List<String> {
       return taskRepositoryBD.getAllCustomerNames()
    }

    fun giveClientName(customerId: CustomerId): String{
        return taskRepositoryBD.getCustomerById(customerId)?.name ?: "Null"
    }

    fun addTaskRepository(task: Task){
        taskRepositoryBD.insert(task)
    }

    fun updateTask(task: Task){
        taskRepositoryBD.update(task)
    }

    fun setEditorMode(editorMode: Boolean){
        isEditor.value = editorMode
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