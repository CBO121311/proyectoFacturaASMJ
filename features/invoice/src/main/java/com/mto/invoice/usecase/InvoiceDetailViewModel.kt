package com.mto.invoice.usecase


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moronlu18.accounts.entity.Invoice
import com.moronlu18.accounts.entity.Item
import com.moronlu18.accounts.repository.InvoiceProvider
import com.moronlu18.accounts.repository.ItemProvider
import com.mto.invoice.adapter.detail.ItemAdapter

class InvoiceDetailViewModel: ViewModel() {

    var user = MutableLiveData<String>()
    var status = MutableLiveData<String>()
    var startDate = MutableLiveData<String>()
    var endDate = MutableLiveData<String>()
    var adapter = MutableLiveData<ItemAdapter>()
    var total = MutableLiveData<String>()
    private var state = MutableLiveData<InvoiceDetailState>()

    fun giveTotal(lista: MutableList<Item>): String {
        return ItemProvider.getTotal(lista)
    }

    fun getPosByInvoice(invoice: Invoice): Int {
        return InvoiceProvider.getPosByInvoice(invoice)
    }
    fun getInvoiceByPos(posInvoice: Int): Invoice {
        return InvoiceProvider.getInvoicePos(posInvoice)
    }

    fun deleteInvoice(invoice: Invoice) {
        val position = getPosByInvoice(invoice)
        if (position != -1) {
            InvoiceProvider.deleteInvoice(position)
        }
    }
    fun onSuccess() {
        state.value = InvoiceDetailState.OnSuccess
    }
    fun getState(): LiveData<InvoiceDetailState> {
        return state
    }
}