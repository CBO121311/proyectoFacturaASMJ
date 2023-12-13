package com.sergiogv98.usecase

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moronlu18.accounts.entity.Customer
import com.moronlu18.accounts.entity.Item
import com.moronlu18.accounts.entity.Task
import com.moronlu18.accounts.repository.CustomerProvider
import com.moronlu18.accounts.repository.ItemProvider
import com.moronlu18.accounts.repository.TaskProvider
import com.sergiogv98.tasklist.ui.TaskState
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class TaskViewModel : ViewModel() {

    var taskName = MutableLiveData<String>()
    var customerName = MutableLiveData<String>()
    var dateCreation = MutableLiveData<String>()
    var dateEnd = MutableLiveData<String>()

    private var state = MutableLiveData<TaskState>()

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
        return CustomerProvider.getCustomer().find { it.name == nameCustomer }
    }

    fun giveListCustomer() : List<String> {
        return CustomerProvider.getCustomer().map { it.name }
    }

    fun addTaskRepository(task: Task){
        TaskProvider.taskDataSet.add(task)
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