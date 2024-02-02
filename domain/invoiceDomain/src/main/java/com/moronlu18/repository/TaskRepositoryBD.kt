package com.moronlu18.repository

import android.database.sqlite.SQLiteException
import com.moronlu18.data.base.TaskId
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

    fun getTaskById(taskId: TaskId): Task? {
        return InvoiceDatabase.getInstance().taskDao().getTaskById(taskId)
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
}