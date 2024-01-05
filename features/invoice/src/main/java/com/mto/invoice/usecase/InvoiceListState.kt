package com.mto.invoice.usecase

import com.moronlu18.accounts.entity.Factura

sealed class InvoiceListState {
    data object NoDataSet: InvoiceListState()
    data class Success(val dataset: ArrayList<Factura>) : InvoiceListState()
    data class Loading(val value: Boolean) : InvoiceListState()


}