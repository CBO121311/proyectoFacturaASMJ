package com.moronlu18.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.moronlu18.accounts.entity.Item
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
    @Query("select * from invoice")
    fun selectAll(): Flow<List<Invoice>>
    @Query("SELECT * FROM invoice WHERE id = :invoiceId")
    fun getInvoiceById(invoiceId: Int): Invoice?
    @Query("SELECT MAX(id) FROM invoice")
    fun getLastInvoiceId(): Int?

    @Query("SELECT * FROM item join line_item where id == itemId and invoiceId == :idInv")
    fun getItemListById(idInv:Int): List<Item>
    @Query("SELECT name FROM customer where id = :id")
    fun getCustomerNameById(id:Int): String

    @Query("SELECT number FROM invoice")
    fun getListNumber(): List<String>

    @Query("SELECT EXISTS(SELECT 1 FROM invoice WHERE customerId = :customerId)")
    fun customerExistInvoice(customerId: Int): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM line_item WHERE itemId = :itemId)")
    fun itemExistsLineItem(itemId: ItemId): Boolean
}
