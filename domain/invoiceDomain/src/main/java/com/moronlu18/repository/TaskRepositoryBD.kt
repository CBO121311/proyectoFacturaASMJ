package com.moronlu18.repository

import android.database.sqlite.SQLiteException
import com.moronlu18.data.base.CustomerId
import com.moronlu18.data.task.Task
import com.moronlu18.database.InvoiceDatabase
import com.moronlu18.network.Resource
import com.moronlu18.network.Resource.Success
import kotlinx.coroutines.flow.Flow

class TaskRepositoryBD {
    fun insert(task: Task): Resource{
        try {
            InvoiceDatabase.getInstance().taskDao().insert(task)
        } catch (e: SQLiteException){
            return Resource.Error(e)
        }
        return Success(task)
    }
    fun update(task: Task): Resource {
        try {

            InvoiceDatabase.getInstance().taskDao().update(task)


        } catch (e: SQLiteException) {
            return Resource.Error(e)
        }
        return Success(task)
    }

    fun getLastTaskId(): Int? {
        return InvoiceDatabase.getInstance().taskDao().getLastTaskId()
    }

    fun getTaskList(): Flow<List<Task>>{
        return InvoiceDatabase.getInstance().taskDao().selectAll()
    }

    fun delete (task: Task){
        InvoiceDatabase.getInstance().taskDao().delete(task)
    }

    fun getCustomerNameById(customerId: Int): String{
        return InvoiceDatabase.getInstance().taskDao().getCustomerNameById(customerId)
    }

    // Configuracion
    fun getTaskListById(): Flow<List<Task>>{
        return InvoiceDatabase.getInstance().taskDao().selectAllById()
    }

    fun getTaskOrderByName(): Flow<List<Task>>{
        return InvoiceDatabase.getInstance().taskDao().selectAllByName()
    }

    fun getTaskOrderByCustomerName(): Flow<List<Task>> {
        return InvoiceDatabase.getInstance().taskDao().selectTasksOrderedByCustomerName()
    }

    fun getTaskOrderByCustomerNameDesc(): Flow<List<Task>> {
        return InvoiceDatabase.getInstance().taskDao().selectTasksOrderedByCustomerNameDesc()
    }

    fun customerExistTask(customerId: CustomerId): Boolean{
        return InvoiceDatabase.getInstance().taskDao().customerExistTask(customerId.value)
    }
}