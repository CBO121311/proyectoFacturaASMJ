package com.moronlu18.repository

import com.moronlu18.data.base.TaskId
import com.moronlu18.data.task.Task
import com.moronlu18.data.task.TaskStatus
import com.moronlu18.data.task.TypeTask
import com.moronlu18.network.ResourceList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.time.Instant

class TaskProvider {

    companion object {

        var taskDataSet: MutableList<Task> = mutableListOf()
        private var idTask: Int = 1

        init {
            initDataSetTask()
        }

        private fun initDataSetTask() {
            taskDataSet.add(
                Task(
                    TaskId(idTask++),
                    CustomerProvider.CustomerdataSet[1].id,
                    "Hacer la presentación",
                    TypeTask.LLAMAR,
                    TaskStatus.PENDIENTE,
                    "Preparar la presentación para la reunión de ventas",
                    Instant.parse("2023-11-15T00:00:00Z"),
                    Instant.parse("2023-11-15T00:00:00Z")
                ),
            )
            taskDataSet.add(
                Task(
                    TaskId(idTask++),
                    CustomerProvider.CustomerdataSet[2].id,
                    "Completar informe",
                    TypeTask.LLAMAR,
                    TaskStatus.PENDIENTE,
                    "Finalizar el informe mensual de ventas y enviarlo al cliente",
                    Instant.parse("2023-11-15T00:00:00Z"),
                    Instant.parse("2023-11-15T00:00:00Z"),
                ),
            )
            taskDataSet.add(
                Task(
                    TaskId(idTask++),
                    CustomerProvider.CustomerdataSet[3].id,
                    "Entrenamiento en línea",
                    TypeTask.VISITA,
                    TaskStatus.VENCIDA,
                    "Participar en el curso de desarrollo web en línea",
                    Instant.parse("2023-11-15T00:00:00Z"),
                    Instant.parse("2023-11-15T00:00:00Z"),
                ),
            )
        }

        suspend fun getTaskList(): ResourceList {
            return withContext(Dispatchers.IO) {
                delay(2000)
                when {
                    taskDataSet.isEmpty() -> ResourceList.Error(Exception("Lista de Tareas vacia"))
                    else -> ResourceList.Success(taskDataSet as ArrayList<Task>)
                }
            }
        }

        suspend fun getTaskListNoCharge(): ResourceList {
            return withContext(Dispatchers.IO) {
                when {
                    taskDataSet.isEmpty() -> ResourceList.Error(Exception("Lista de Tareas vacia"))
                    else -> ResourceList.Success(taskDataSet as ArrayList<Task>)
                }
            }
        }

        fun getTaskPosition(position: Int): Task {
            return taskDataSet[position]
        }

        fun getTasks(): List<Task> {
            return taskDataSet
        }

        fun updateTasks(task: Task, position: Int){
            taskDataSet[position] = task
        }

        fun getPositionByTask(task: Task): Int{
            return taskDataSet.indexOf(task)
        }

        /**
         * Comprueba si el id de cliente está en Task
         */
        fun isCustomerReferenceTask(idCli: Int): Boolean {
            return taskDataSet.any() { it.customerId.value == idCli }
        }
    }
}