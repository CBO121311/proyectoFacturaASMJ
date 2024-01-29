package com.moronlu18.data.converter

import androidx.room.TypeConverters
import com.moronlu18.data.task.TaskStatus
import com.moronlu18.data.task.TypeTask

class StatusTypeConverter {
    @TypeConverters
    fun toTypeTask(value: Int) : TypeTask {
        return enumValues<TypeTask>()[value]
    }

    @TypeConverters
    fun fromTypeTask(value: TypeTask) : Int {
        return value.ordinal
    }

    @TypeConverters
    fun toStatusTask(value: Int) : TaskStatus {
        return enumValues<TaskStatus>()[value]
    }

    @TypeConverters
    fun fromStatusTask(value: TaskStatus) : Int {
        return value.ordinal
    }
}