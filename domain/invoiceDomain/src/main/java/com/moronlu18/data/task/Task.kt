package com.moronlu18.data.task

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.moronlu18.data.base.CustomerId
import com.moronlu18.data.customer.Customer
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.time.Instant

@Entity(tableName = "task")
@Parcelize
data class Task(
    @PrimaryKey
    val id: Int,
    val customerId: @RawValue CustomerId,
    val nomTask: String,
    val typeTask: TypeTask,
    val taskStatus: TaskStatus,
    val descTask: String? = null,
    val dateCreation: Instant,
    val dateFinalization: Instant
) : Parcelable