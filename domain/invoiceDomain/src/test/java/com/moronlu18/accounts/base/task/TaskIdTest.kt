package com.moronlu18.accounts.base.task

import com.moronlu18.data.base.TaskId
import junit.framework.TestCase.assertEquals
import org.junit.Test

class TaskIdTest {

    @Test
    fun `test creacion de taskId`(){
        val value = 1
        val taskId = TaskId(value)
        assertEquals(value, taskId.value)
    }

    @Test
    fun `test compareTo de taskId`(){
        val taskId1 = TaskId(1)

        assertEquals(0, taskId1.compareTo(taskId1))
    }

}