package com.moronlu18.data.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.moronlu18.data.task.TaskStatus

@ProvidedTypeConverter
class TaskStatusConverter {

    @TypeConverter
    fun toStatusTask(value: Int) : TaskStatus {
        return enumValues<TaskStatus>()[value]
    }

    @TypeConverter
    fun fromStatusTask(value: TaskStatus) : Int {
        return value.ordinal
    }
}