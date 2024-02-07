package com.moronlu18.repository

import android.database.sqlite.SQLiteException
import com.moronlu18.data.base.CustomerId
import com.moronlu18.data.customer.Customer
import com.moronlu18.data.task.Task
import com.moronlu18.database.InvoiceDatabase
import com.moronlu18.network.Resource
import com.moronlu18.network.Resource.Success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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

    fun getAllCustomerNames(): List<String>{
        return InvoiceDatabase.getInstance().taskDao().getAllCustomerNames()
    }

    fun getCustomerById(customerId: CustomerId): Customer? {
        return InvoiceDatabase.getInstance().taskDao().selectCustomerById(customerId)
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

    // Configuracion
    fun getTaskListById(): Flow<List<Task>>{
        return InvoiceDatabase.getInstance().taskDao().selectAllById()
    }

    fun getTaskOrderByName(): Flow<List<Task>>{
        return InvoiceDatabase.getInstance().taskDao().selectAllByName()
    }

    fun getTaskOrderByCustomerNameDesc(): Flow<List<Task>> {
        return InvoiceDatabase.getInstance().taskDao().selectAllCustomerName()
            .map { customers ->
                val tasksSortedByCustomerName = mutableListOf<Task>()
                customers.forEach { customer ->
                    val tasksForCustomer = InvoiceDatabase.getInstance().taskDao().selectTasksByCustomerId(customer.id)
                    tasksSortedByCustomerName.addAll(tasksForCustomer)
                }
                tasksSortedByCustomerName.sortedBy { task ->
                    customers.find { it.id == task.customerId }?.name
                }
                tasksSortedByCustomerName.toList()
            }
    }


    fun getTaskOrderByCustomerName(): Flow<List<Task>> {
        return InvoiceDatabase.getInstance().taskDao().selectAllCustomerNameDesc()
            .map { customers ->
                val tasksSortedByCustomerNameDesc = mutableListOf<Task>()
                customers.forEach { customer ->
                    val tasksForCustomer = InvoiceDatabase.getInstance().taskDao().selectTasksByCustomerId(customer.id)
                    tasksSortedByCustomerNameDesc.addAll(tasksForCustomer)
                }
                tasksSortedByCustomerNameDesc.sortedByDescending { task ->
                    customers.find { it.id == task.customerId }?.name
                }
                tasksSortedByCustomerNameDesc.toList()
            }
    }
}