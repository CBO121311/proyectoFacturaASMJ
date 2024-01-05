package com.mto.invoice.usecase

sealed class InvoiceDetailState {
    data object OnSuccess : InvoiceDetailState()
}
