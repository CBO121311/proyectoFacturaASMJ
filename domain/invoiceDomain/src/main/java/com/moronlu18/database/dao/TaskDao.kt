package com.moronlu18.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.moronlu18.data.base.TaskId
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

    @Query("SELECT MAX(id) FROM task")
    fun getLastTaskId(): Int?

    @Delete
    fun delete(task: Task)
}