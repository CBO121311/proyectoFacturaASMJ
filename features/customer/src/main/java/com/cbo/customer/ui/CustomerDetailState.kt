package com.cbo.customer.ui

sealed class CustomerDetailState{
    data object ReferencedCustomer: CustomerDetailState()
    data object OnSuccess: CustomerDetailState()
}
