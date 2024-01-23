package com.moronlu18.data.task

import android.os.Parcelable
import com.moronlu18.data.customer.Customer
import com.moronlu18.data.base.Entity
import kotlinx.parcelize.Parcelize
import java.time.Instant

@Parcelize
data class Task(
    override val id: Int,
    val customerId: Customer,
    val nomTask: String,
    val typeTask: TypeTask,
    val taskStatus: TaskStatus,
    val descTask: String? = null,
    val dateCreation: Instant,
    val dateFinalization: Instant
) : Parcelable, Entity<Int>(id)