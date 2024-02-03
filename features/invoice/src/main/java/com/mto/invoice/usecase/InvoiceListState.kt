package com.mto.invoice.usecase

sealed class InvoiceListState {
    data object NoDataSet: InvoiceListState()
    data object Success: InvoiceListState()
    data class Loading(val value: Boolean) : InvoiceListState()


}