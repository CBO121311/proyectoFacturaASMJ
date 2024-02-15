package com.moronlu18.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.moronlu18.data.base.CustomerId
import com.moronlu18.data.customer.Customer
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insert(customer: Customer): Long

    @Query ("SELECT * FROM customer ORDER BY id")
    fun  selectAll(): Flow<List<Customer>>
    @Query ("SELECT * FROM customer ORDER BY LOWER(name) ASC")
    fun  selectAllName(): Flow<List<Customer>>

    @Query ("SELECT * FROM customer ORDER BY LOWER(name) COLLATE NOCASE DESC")
    fun  selectAllNameDesc(): Flow<List<Customer>>

    @Query ("SELECT * FROM customer ORDER BY LOWER(email) ASC")
    fun  selectAllEmail(): Flow<List<Customer>>

    @Query("SELECT name FROM customer WHERE id = :customerId")
    fun getNameById(customerId: CustomerId): String?

    @Query("SELECT MAX(id) FROM customer")
    fun getLastCustomerId(): Int?

    @Query("SELECT * FROM customer WHERE id = :customerId")
    fun selectCustomerById(customerId: CustomerId): Customer?

    @Update
    fun update(customer: Customer)

    @Delete
    fun delete(customer: Customer)

    //Task
    @Query("SELECT * FROM customer")
    fun getAllCustomer(): List<Customer>

    @Query("SELECT name FROM customer")
    fun getAllCustomerNames(): List<String>

    @Query("SELECT id FROM customer WHERE name = :customerName")
    fun getCustomerIdByName(customerName: String): CustomerId
}