package com.moronlu18.data.task

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.moronlu18.data.base.CustomerId
import com.moronlu18.data.base.TaskId
import com.moronlu18.data.converter.StatusTypeConverter
import com.moronlu18.data.customer.Customer
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.time.Instant

@Entity(tableName = "task", foreignKeys = [
    ForeignKey(
        entity = Customer::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("customerId"),
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE
    )
])
@Parcelize
data class Task(
    @PrimaryKey
    val id: @RawValue TaskId,
    val customerId: @RawValue CustomerId,
    val nomTask: String,
    @TypeConverters(StatusTypeConverter::class)
    val typeTask: TypeTask,
    @TypeConverters(StatusTypeConverter::class)
    val taskStatus: TaskStatus,
    val descTask: String? = null,
    val dateCreation: Instant,
    val dateFinalization: Instant
) : Parcelable