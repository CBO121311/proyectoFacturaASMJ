package com.moronlu18.data.task

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.moronlu18.data.base.CustomerId
import com.moronlu18.data.base.TaskId
import com.moronlu18.data.converter.CustomerIdTypeConverter
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
    @TypeConverters(CustomerIdTypeConverter::class)
    var customerId: @RawValue CustomerId,
    var nomTask: String,
    @TypeConverters(TaskTypeConverter::class)
    var typeTask: TypeTask,
    @TypeConverters(TaskStatusConverter::class)
    var taskStatus: TaskStatus,
    var descTask: String? = null,
    @TypeConverters(InstantConverter::class)
    var dateCreation: Instant,
    @TypeConverters(InstantConverter::class)
    var dateFinalization: Instant
) : Parcelable, Comparable<Task> {
    override fun compareTo(other: Task): Int {
        return nomTask.lowercase().compareTo(other.nomTask.lowercase())
    }

    companion object {
        fun create(
            id: Int,
            customerId: CustomerId,
            nameTask: String,
            typeTask: TypeTask,
            taskStatus: TaskStatus,
            descTask: String?,
            dateCreation: Instant,
            dateFinalization: Instant
        ): Task {
            return Task(
                id = TaskId(id),
                customerId = customerId,
                nomTask = nameTask,
                typeTask = typeTask,
                taskStatus = taskStatus,
                descTask = descTask,
                dateCreation = dateCreation,
                dateFinalization = dateFinalization
            )
        }
    }
}