package com.cbo.customer.ui


sealed class CustomerListState {

    data object NoDataError : CustomerListState()
    data object ReferencedCustomer : CustomerListState()
    data object Success : CustomerListState()
    data class Loading(val value: Boolean) : CustomerListState()
}