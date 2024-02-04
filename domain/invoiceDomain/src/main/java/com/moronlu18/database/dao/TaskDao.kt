package com.moronlu18.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.moronlu18.data.base.TaskId
import com.moronlu18.data.customer.Customer
import com.moronlu18.data.task.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(task: Task) : Long

    @Query("SELECT * FROM task")
    fun selectAll(): Flow<List<Task>>

    @Query("SELECT * FROM task WHERE id = :taskId")
    fun getTaskById(taskId: TaskId): Task?

    @Query("SELECT rowid FROM task WHERE id = :taskId")
    fun getPositionByTaskId(taskId: TaskId): Int?

    @Query("SELECT * FROM task ORDER BY id ASC LIMIT 1 OFFSET :position")
    fun getTaskByPosition(position: Int): Task?

    @Query("SELECT MAX(id) FROM task")
    fun getLastTaskId(): Int?

    @Update
    fun update(task: Task)

    @Delete
    fun delete(task: Task)
}