package com.moronlu18.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.moronlu18.data.account.Account
import com.moronlu18.data.account.BusinessProfile
import com.moronlu18.data.invoice.Invoice
import com.moronlu18.data.invoice.LineItem


@Dao
interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(account: Account): Long

    //en los update se reemplaza
    @Update
    fun update(account: Account)

    //Se añada una query personalizada en un método que devuelve un objeto Account y el objeto
    // BusinesProfile.

    //hace un join de cuenta y businessprofile

    @Query("SELECT * FROM account")
    fun selectAll(): List<Account> // recoger el id

    @Query("SELECT * FROM account JOIN businessprofile ON account.businessProfile =businessprofile.id")
    fun loadAccountAndBusinessProfile(): Map<Account, BusinessProfile> // me devuelve account y perfil dentro de un mapa
    //  El de arriba es 1:1
    //si tuvieramos una relación de 1.N invoice JOin invoice on objeto

    //account.businessProfileId =businessprofile.id
}


@Dao
interface InvoiceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(invoice: Invoice): Long

    //relacion 1 a N
    //@Query("SELECT * FROM invoice JOIN lineitem ON invoice.id = lineitem.idInvoice")
    fun loadInvoiceAndLineItem(): Map<Invoice, List<LineItem>> // me devuelve account y perfil dentro de un mapa

}
