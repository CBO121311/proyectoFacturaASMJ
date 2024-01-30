package com.moronlu18.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.moronlu18.data.customer.Customer
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insert(customer: Customer): Long
    @Query ("SELECT * FROM customer")
    fun  selectAll(): Flow<List<Customer>>

    @Delete
    fun delete(customer: Customer)
}