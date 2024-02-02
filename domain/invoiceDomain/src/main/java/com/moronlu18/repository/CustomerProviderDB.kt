package com.moronlu18.repository

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

    fun delete(customer: Customer) {
        InvoiceDatabase.getInstance().customerDao().delete(customer)
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
        return InvoiceDatabase.getInstance().customerDao().getCustomerById(customerId)
    }

    fun getLastCustomerId(): Int? {
        return InvoiceDatabase.getInstance().customerDao().getLastCustomerId()
    }


}