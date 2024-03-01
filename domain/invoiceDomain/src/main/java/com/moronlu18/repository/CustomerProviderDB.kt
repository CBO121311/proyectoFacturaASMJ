package com.moronlu18.repository

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import com.moronlu18.data.base.CustomerId
import com.moronlu18.data.customer.Customer
import com.moronlu18.database.InvoiceDatabase
import com.moronlu18.network.Resource
import kotlinx.coroutines.flow.Flow

class CustomerProviderDB {
    fun insert(customer: Customer): Resource {
        try {
            InvoiceDatabase.getInstance().customerDao().insert(customer)
        } catch (e: SQLiteException) {

            return Resource.Error(e)
        }
        return Resource.Success(Customer)
    }

    fun getCustomerList(): Flow<List<Customer>> {
        return InvoiceDatabase.getInstance().customerDao().selectAll()
    }
    fun getCustomerListName(): Flow<List<Customer>> {
        return InvoiceDatabase.getInstance().customerDao().selectAllName()
    }
    fun getCustomerListNameDesc(): Flow<List<Customer>> {
        return InvoiceDatabase.getInstance().customerDao().selectAllNameDesc()
    }
    fun getCustomerListEmail(): Flow<List<Customer>> {
        return InvoiceDatabase.getInstance().customerDao().selectAllEmail()
    }

    fun delete(customer: Customer): Resource {
        try {
            InvoiceDatabase.getInstance().customerDao().delete(customer)

        } catch (e: SQLiteConstraintException) {
            return Resource.Error(e)
        }
        return Resource.Success(null)
    }

    fun update(customer: Customer): Resource {
        try {

            InvoiceDatabase.getInstance().customerDao().update(customer)

        } catch (e: SQLiteException) {
            return Resource.Error(e)
        }
        return Resource.Success(customer)
    }

    fun getLastCustomerId(): Int? {
        return InvoiceDatabase.getInstance().customerDao().getLastCustomerId()
    }

    fun getAllCustomerNames(): List<String>{
        return InvoiceDatabase.getInstance().customerDao().getAllCustomerNames()
    }

    fun getCustomerById(customerId: CustomerId): Customer? {
        return InvoiceDatabase.getInstance().customerDao().selectCustomerById(customerId)
    }

    fun getCustomerIdByName(customerName: String): CustomerId{
        return InvoiceDatabase.getInstance().customerDao().getCustomerIdByName(customerName)
    }

    fun customerExistInvoice(customerId: CustomerId): Boolean{
        return InvoiceDatabase.getInstance().invoiceDao().customerExistInvoice(customerId.value)
    }

    fun customerExistTask(customerId: CustomerId): Boolean{
        return InvoiceDatabase.getInstance().taskDao().customerExistTask(customerId.value)
    }
}