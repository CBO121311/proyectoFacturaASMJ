package com.moronlu18.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.moronlu18.data.invoice.LineItem

@Dao
interface lineItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(lineItem: LineItem): Long

    @Delete
    fun delete(lineItem: LineItem)

    @Query("SELECT name FROM customer where id = :id")
    fun getCustomerNameById(id:Int): String

}