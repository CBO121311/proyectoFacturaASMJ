package com.moronlu18.accounts.repository

import com.moronlu18.accounts.entity.Task
import com.moronlu18.accounts.enum.TaskStatus
import com.moronlu18.accounts.enum.TypeTask
import java.time.Instant

class TaskProvider {

    companion object {

        var taskDataSet: MutableList<Task> = mutableListOf()

        init {
            initDataSetTask()
        }

        private fun initDataSetTask() {

        }
    }
}