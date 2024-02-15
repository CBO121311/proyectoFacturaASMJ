package com.moronlu18.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.moronlu18.accounts.entity.Item
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Insert(onConflict = ForeignKey.RESTRICT)
    fun insert(item: Item): Long

    @Update
    fun update(item: Item)

    @Query("SELECT * FROM item ORDER BY id")
    fun selectAll(): Flow<List<Item>>

    @Query("SELECT MAX(id) FROM item")
    fun getLastItemId(): Int?

    @Query("SELECT * FROM item ORDER BY name")
    fun selectAllName(): Flow<List<Item>>

    @Query("SELECT * FROM item ORDER BY price")
    fun selectAllPrice(): Flow<List<Item>>

    @Query("SELECT * FROM item ORDER BY price DESC")
    fun selectAllPriceDesc(): Flow<List<Item>>

    @Delete
    fun delete(item: Item)
}