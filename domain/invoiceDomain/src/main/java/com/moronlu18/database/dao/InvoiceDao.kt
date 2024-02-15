package com.moronlu18.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.moronlu18.data.base.ItemId
import com.moronlu18.data.invoice.Invoice
import kotlinx.coroutines.flow.Flow

@Dao
interface InvoiceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(invoice: Invoice): Long

    @Update
    fun update(invoice: Invoice)

    @Delete
    fun delete(invoice: Invoice)

    /*
    @Query("select * from invoice join line_item on invoice.id == line_item.invoiceId")
    fun loadInvoiceAndLineItem(): Map<Invoice, List<LineItem>>
     */
    @Query("select * from invoice order by id")
    fun selectAll(): Flow<List<Invoice>>

    @Query("SELECT * FROM invoice INNER JOIN customer ON invoice.customerId = customer.id ORDER BY LOWER(customer.name) ASC")
    fun getInvoiceByNameAZ(): Flow<List<Invoice>>
    @Query("SELECT * FROM invoice INNER JOIN customer ON invoice.customerId = customer.id ORDER BY LOWER(customer.name) DESC")
    fun getInvoiceByNameZA(): Flow<List<Invoice>>
    @Query("SELECT * FROM invoice order by status")
    fun getInvoiceByStatus(): Flow<List<Invoice>>
    @Query("SELECT * FROM invoice WHERE id = :invoiceId")
    fun getInvoiceById(invoiceId: Int): Invoice?
    @Query("SELECT MAX(id) FROM invoice")
    fun getLastInvoiceId(): Int?


    @Query("SELECT number FROM invoice")
    fun getListNumber(): List<String>

    @Query("SELECT EXISTS(SELECT 1 FROM invoice WHERE customerId = :customerId)")
    fun customerExistInvoice(customerId: Int): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM line_item WHERE itemId = :itemId)")
    fun itemExistsLineItem(itemId: ItemId): Boolean
}
