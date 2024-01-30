package com.moronlu18.data.task

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.moronlu18.data.base.CustomerId
import com.moronlu18.data.base.TaskId
import com.moronlu18.data.converter.InstantConverter
import com.moronlu18.data.converter.TaskIdTypeConverter
import com.moronlu18.data.converter.TaskStatusConverter
import com.moronlu18.data.converter.TaskTypeConverter
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
    @TypeConverters(TaskIdTypeConverter::class)
    val id: @RawValue TaskId,
    val customerId: @RawValue CustomerId,
    val nomTask: String,
    @TypeConverters(TaskTypeConverter::class)
    val typeTask: TypeTask,
    @TypeConverters(TaskStatusConverter::class)
    val taskStatus: TaskStatus,
    val descTask: String? = null,
    @TypeConverters(InstantConverter::class)
    val dateCreation: Instant,
    @TypeConverters(InstantConverter::class)
    val dateFinalization: Instant
) : Parcelable