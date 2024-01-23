package com.cbo.customer.ui

import com.moronlu18.data.customer.Customer

sealed class CustomerListState {

    data object NoDataError : CustomerListState()
    data object ReferencedCustomer: CustomerListState()
    data class Success(val dataset: ArrayList<Customer>) : CustomerListState()
    data class Loading(val value: Boolean) : CustomerListState()
}