package com.cbo.customer.ui


sealed class CustomerCreationState {
    data object NameIsMandatory : CustomerCreationState()
    data object EmailEmptyError : CustomerCreationState()
    data object InvalidId : CustomerCreationState()
    data object InvalidEmailFormat : CustomerCreationState()
    data object OnSuccess : CustomerCreationState()
}