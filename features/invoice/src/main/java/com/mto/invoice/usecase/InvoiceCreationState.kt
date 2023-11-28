package com.mto.invoice.usecase

sealed class InvoiceCreationState {

    data object CustomerUnspecified: InvoiceCreationState()
    data object CustomerNoExists: InvoiceCreationState()
    data object AtLeastOneItem: InvoiceCreationState()
    data object IncorrectDateRange: InvoiceCreationState()

    data object StartDateEmptyError: InvoiceCreationState()
    data object EndDateEmptyError: InvoiceCreationState()

    data object StartDateFormatError: InvoiceCreationState()
    data object EndDateFormatError: InvoiceCreationState()

    data object OnSuccess : InvoiceCreationState()
}