package com.sergiogv98.utils

import com.moronlu18.data.task.TaskStatus
import com.moronlu18.data.task.TypeTask
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.TimeZone

class TaskUtils {

    private fun formatStatus(status: TaskStatus): String {
        return status.name.lowercase().replaceFirstChar { it.uppercase() }
    }

    fun formatType(status: TypeTask): String {
        return status.name.lowercase().replaceFirstChar { it.uppercase() }
    }

    fun convertInstantToDateText(instant: Instant): String {
        val timeZone = TimeZone.getDefault().toZoneId()
        val localDateTime = LocalDateTime.ofInstant(instant, timeZone)
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return localDateTime.format(dateFormatter)
    }

    fun taskStatusGive(dateEnd: Instant, taskStatus: TaskStatus): String {
        val currentDate = Instant.now()

        return if (currentDate.isAfter(dateEnd)) {
            formatStatus(TaskStatus.VENCIDA)
        } else {
            formatStatus(taskStatus)
        }
    }

}