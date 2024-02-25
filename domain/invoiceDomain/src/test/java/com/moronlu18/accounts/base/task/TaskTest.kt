package com.moronlu18.accounts.base.task

import com.moronlu18.data.base.CustomerId
import com.moronlu18.data.base.TaskId
import com.moronlu18.data.task.Task
import com.moronlu18.data.task.TaskStatus
import com.moronlu18.data.task.TypeTask
import org.junit.Test
import java.time.Instant
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue

class TaskTest {

    @Test
    fun `test crear una tarea`(){
        val id = TaskId(1)
        val customerId = CustomerId(1)
        val nomTask = "TaskTest"
        val typeTask = TypeTask.LLAMAR
        val taskStatus = TaskStatus.PENDIENTE
        val descTask = "Junit test"
        val dateCreation = Instant.parse("2023-11-15T00:00:00Z")
        val dateFinalization = Instant.parse("2023-11-15T00:00:00Z")

        val task = Task.create(id.value, customerId, nomTask, typeTask, taskStatus, descTask, dateCreation, dateFinalization)

        assertEquals(id, task.id)
        assertEquals(customerId, task.customerId)
        assertEquals(nomTask, task.nomTask)
        assertEquals(typeTask, task.typeTask)
        assertEquals(taskStatus, task.taskStatus)
        assertEquals(descTask, task.descTask)
        assertEquals(dateCreation, task.dateCreation)
        assertEquals(dateFinalization, task.dateFinalization)
    }

    @Test
    fun `test compareTo nombre con otro nombre de tarea`(){
        val id = TaskId(1)
        val customerId = CustomerId(1)
        val nomTask = "TaskTest"
        val typeTask = TypeTask.LLAMAR
        val taskStatus = TaskStatus.PENDIENTE
        val descTask = "Junit test"
        val dateCreation = Instant.parse("2023-11-15T00:00:00Z")
        val dateFinalization = Instant.parse("2023-11-15T00:00:00Z")

        val task = Task.create(id.value, customerId, nomTask, typeTask, taskStatus, descTask, dateCreation, dateFinalization)

        val id2 = TaskId(2)
        val customerId2 = CustomerId(2)
        val nomTask2 = "TaskTest2"
        val typeTask2 = TypeTask.LLAMAR
        val taskStatus2 = TaskStatus.PENDIENTE
        val descTask2 = "Junit test 2"
        val dateCreation2 = Instant.parse("2023-11-15T00:00:00Z")
        val dateFinalization2 = Instant.parse("2023-11-15T00:00:00Z")

        val task2 = Task.create(id2.value, customerId2, nomTask2, typeTask2, taskStatus2, descTask2, dateCreation2, dateFinalization2)

        assertTrue(task.compareTo(task2) < 1)
    }
}