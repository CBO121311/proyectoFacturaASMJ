package com.moronlu18.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.moronlu18.data.invoice.Invoice
import kotlinx.coroutines.flow.Flow

@Dao
interface InvoiceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(invoice: Invoice): Long

    @Update
    fun update(invoice: Invoice)

    /*
    @Query("select * from invoice join line_item on invoice.id == line_item.invoiceId")
    fun loadInvoiceAndLineItem(): Map<Invoice, List<LineItem>>

     */

    @Query("select * from invoice")
    fun selectAll(): Flow<List<Invoice>>

    @Delete
    fun delete(invoice: Invoice)

}
