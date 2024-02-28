package com.moronlu18.repository

import android.database.sqlite.SQLiteException
import com.moronlu18.accounts.entity.Item
import com.moronlu18.data.base.CustomerId
import com.moronlu18.data.base.InvoiceId
import com.moronlu18.data.customer.Customer
import com.moronlu18.data.invoice.Invoice
import com.moronlu18.database.InvoiceDatabase
import com.moronlu18.network.Resource
import kotlinx.coroutines.flow.Flow
import java.lang.NullPointerException
import java.util.Calendar
import kotlin.random.Random

class InvoiceProviderDB {
    companion object {
        fun insert(invoice: Invoice): Resource {
            try {
                InvoiceDatabase.getInstance()?.invoiceDao()?.insert(invoice)
            } catch (e: SQLiteException) {
                return Resource.Error(e)
            }
            return Resource.Success(invoice)
        }

        fun update(invoice: Invoice): Resource {
            try {
                InvoiceDatabase.getInstance().invoiceDao().update(invoice)
            } catch (e: SQLiteException) {
                return Resource.Error(e)
            }
            return Resource.Success(invoice)
        }

        fun delete(invoice: Invoice) {
            InvoiceDatabase.getInstance().invoiceDao().delete(invoice)
        }

        fun getInvoiceListFlow(): Flow<List<Invoice>> {
            return InvoiceDatabase.getInstance().invoiceDao().selectAll()
        }
        fun getInvoiceByNameAZ(): Flow<List<Invoice>> {
            return InvoiceDatabase.getInstance().invoiceDao().getInvoiceByNameAZ()
        }
        fun getInvoiceByNameZA(): Flow<List<Invoice>> {
            return InvoiceDatabase.getInstance().invoiceDao().getInvoiceByNameZA()
        }
        fun getInvoiceByStatus(): Flow<List<Invoice>> {
            return InvoiceDatabase.getInstance().invoiceDao().getInvoiceByStatus()
        }

        fun getInvoiceById(id: Int): Invoice {
            return InvoiceDatabase.getInstance().invoiceDao().getInvoiceById(id)!!

        }

        fun getNewId(): Int {
            return try {
                return InvoiceDatabase.getInstance().invoiceDao().getLastInvoiceId()!!
            } catch (e: NullPointerException) {
                0
            }
        }

        fun getListItem(invoiceId: InvoiceId): List<Item> {
            return InvoiceDatabase.getInstance().itemDao().getItemListById(invoiceId.value)!!
        }

        fun getCustomerNameById(customerId: CustomerId): String? {
            return InvoiceDatabase.getInstance().customerDao().getNameById(customerId)

        }

        fun getListNumber(): List<String>? {
            return InvoiceDatabase.getInstance().invoiceDao().getListNumber()

        }

        /**
         * Función que genera un string aleatorio para el número de la factura
         * que comienza por los cuatro digitos del año actual
         */
        fun generateNumberInvoice(): String {
            val anyoActual = Calendar.getInstance().get(Calendar.YEAR).toString()
            val longitudRestante = 8 - anyoActual.length
            val caracteresAleatorios = (1..longitudRestante)
                .map { Random.nextInt(0, 10).toString() }
                .joinToString("")

            return anyoActual + caracteresAleatorios
        }

        /**
         * Función que asigna un número a la factura comprobando que no
         * exista en ninguna otra
         */
        fun giveNumberInvoice(): String {
            lateinit var numberInvoice: String
            val numbers = getListNumber()
            do {
                numberInvoice = generateNumberInvoice()
            } while (numbers!!.contains(numberInvoice))
            return numberInvoice

        }

        fun getCustomerById(customerId: CustomerId): Customer {
            return InvoiceDatabase.getInstance().customerDao().selectCustomerById(customerId)!!

        }
    }

    fun customerExistInvoice(customerId: CustomerId): Boolean{
        return InvoiceDatabase.getInstance().invoiceDao().customerExistInvoice(customerId.value)
    }
}