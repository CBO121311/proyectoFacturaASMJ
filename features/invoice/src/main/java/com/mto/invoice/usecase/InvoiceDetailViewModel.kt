package com.mto.invoice.usecase


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moronlu18.accounts.entity.Factura
import com.moronlu18.accounts.entity.Item
import com.moronlu18.accounts.repository.CustomerProvider
import com.moronlu18.accounts.repository.FacturaProvider
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

    fun giveNom(id:Int): String {
        return CustomerProvider.getNom(id)

    }

    fun giveTotal(lista: MutableList<Item>): String {
        return ItemProvider.getTotal(lista)
    }

    fun getPosByInvoice(factura: Factura): Int {
        return FacturaProvider.getPosByInvoice(factura)
    }
    fun getInvoiceByPos(posInvoice: Int): Factura {
        return FacturaProvider.getInvoicePos(posInvoice)
    }

    fun deleteInvoice(factura: Factura) {
        val position = getPosByInvoice(factura)
        if (position != -1) {
            FacturaProvider.deleteInvoice(position)
        }
    }
    fun onSuccess() {
        state.value = InvoiceDetailState.OnSuccess
    }
    fun getState(): LiveData<InvoiceDetailState> {
        return state
    }
}