package com.moronlu18.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.Query
import com.moronlu18.data.task.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = ForeignKey.RESTRICT)
    fun insert(task: Task) : Long

    @Query("SELECT * FROM task")
    fun selectAll(): Flow<List<Task>>

    @Delete
    fun delete(task: Task)
}