package com.mto.invoice.usecase

import com.moronlu18.data.invoice.Invoice

sealed class InvoiceListState {
    data object NoDataSet: InvoiceListState()
    data class Success(val dataset: ArrayList<Invoice>) : InvoiceListState()
    data class Loading(val value: Boolean) : InvoiceListState()


}