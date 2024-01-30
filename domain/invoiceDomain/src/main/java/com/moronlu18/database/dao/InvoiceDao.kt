package com.moronlu18.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.moronlu18.data.invoice.Invoice
import com.moronlu18.data.invoice.LineItem

@Dao
interface InvoiceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(invoice: Invoice): Long

    //relacion 1 a N
    //@Query("SELECT * FROM invoice JOIN lineitem ON invoice.id = lineitem.idInvoice")
    fun loadInvoiceAndLineItem(): Map<Invoice, List<LineItem>> // me devuelve account y perfil dentro de un mapa

}
