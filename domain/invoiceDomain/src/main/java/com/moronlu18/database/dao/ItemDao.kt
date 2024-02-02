package com.moronlu18.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.moronlu18.accounts.entity.Item

@Dao
interface ItemDao {
    @Insert(onConflict = ForeignKey.RESTRICT)
    fun insert(item: Item): Long

    @Update
    fun update(item: Item)
/*    @Query("SELECT * FROM item")
    fun selectAll(): List<Item>*/

    @Delete
    fun delete(item: Item)
}