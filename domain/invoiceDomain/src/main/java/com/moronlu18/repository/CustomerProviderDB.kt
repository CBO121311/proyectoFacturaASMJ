package com.moronlu18.repository

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import com.moronlu18.data.base.CustomerId
import com.moronlu18.data.customer.Customer
import com.moronlu18.data.task.Task
import com.moronlu18.database.InvoiceDatabase
import com.moronlu18.network.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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

    fun getAllCustomerNames(): Flow<List<String>>{
        return InvoiceDatabase.getInstance().customerDao().getAllCustomerNames()
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

    fun getCustomerById(customerId: CustomerId): Customer? {
        return InvoiceDatabase.getInstance().customerDao().selectCustomerById(customerId)
    }

    fun getLastCustomerId(): Int? {
        return InvoiceDatabase.getInstance().customerDao().getLastCustomerId()
    }
}