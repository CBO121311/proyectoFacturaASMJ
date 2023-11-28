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
            taskDataSet.add(
                Task(
                    1,
                    "Alice",
                    "Hacer la presentación",
                    TypeTask.TAREA_DE_APRENDIZAJE,
                    TaskStatus.PENDIENTE,
                    "Preparar la presentación para la reunión de ventas",
                    //Instant.parse("2023-11-15"),
                    //Instant.parse("2023-11-15").,
                ),
            )

            taskDataSet.add(
                Task(
                    2,
                    "Bob",
                    "Completar informe",
                    TypeTask.TAREA_DE_APRENDIZAJE,
                    TaskStatus.PENDIENTE,
                    "Finalizar el informe mensual de ventas y enviarlo al cliente",
                    //Instant.parse("2023-11-15"),
                    //Instant.parse("2023-11-15"),
                ),
            )

            taskDataSet.add(
                Task(
                    3,
                    "Charlie",
                    "Entrenamiento en línea",
                    TypeTask.TAREA_DE_BRICOLAJE_Y_REPARACIONES,
                    TaskStatus.VENCIDA,
                    "Participar en el curso de desarrollo web en línea",
                    //Instant.parse("2023-11-15"),
                    //Instant.parse("2023-11-15"),
                ),
            )
        }

        suspend fun isCustomerReferenceTask(nomCli: String?): Boolean {
            return taskDataSet.any() { it.nomClient == nomCli }
        }
    }
}