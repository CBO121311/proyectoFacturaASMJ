package com.moronlu18.accounts.entity

import com.moronlu18.accounts.enum.TaskStatus
import com.moronlu18.accounts.enum.TypeTask

data class Task(
    val id: Int,
    val nomClient: String,
    val nomTask: String,
    val typeTask: TypeTask,
    val taskStatus: TaskStatus,
    val descTask: String,
    val fechCreation: String,
    val fechFinalization: String
)