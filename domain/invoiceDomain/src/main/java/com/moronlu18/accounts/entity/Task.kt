package com.moronlu18.accounts.entity

import android.os.Parcelable
import com.moronlu18.accounts.enum_entity.TaskStatus
import com.moronlu18.accounts.enum_entity.TypeTask
import kotlinx.parcelize.Parcelize
import java.time.Instant

@Parcelize
data class Task(
    val id: Int,
    val clientID: Customer,
    val nomTask: String,
    val typeTask: TypeTask,
    val taskStatus: TaskStatus,
    val descTask: String,
    val fechCreation: Instant,
    val fechFinalization: Instant
) : Parcelable