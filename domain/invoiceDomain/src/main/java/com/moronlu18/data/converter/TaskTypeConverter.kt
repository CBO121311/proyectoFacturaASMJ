package com.moronlu18.data.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.moronlu18.data.task.TypeTask

@ProvidedTypeConverter
class TaskTypeConverter {
    @TypeConverter
    fun toTypeTask(value: Int) : TypeTask {
        return enumValues<TypeTask>()[value]
    }

    @TypeConverter
    fun fromTypeTask(value: TypeTask) : Int {
        return value.ordinal
    }
}