package com.mto.invoice.usecase


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moronlu18.accounts.entity.Item
import com.moronlu18.data.base.CustomerId
import com.moronlu18.data.base.InvoiceId
import com.moronlu18.data.customer.Customer
import com.moronlu18.data.invoice.Invoice
import com.moronlu18.repository.InvoiceProviderDB
import com.moronlu18.repository.LineItemProviderDB
import com.mto.invoice.adapter.detail.ItemAdapter

class InvoiceDetailViewModel : ViewModel() {

    var user = MutableLiveData<String>()
    var status = MutableLiveData<String>()
    var startDate = MutableLiveData<String>()
    var endDate = MutableLiveData<String>()
    var number = MutableLiveData<String>()
    var adapter = MutableLiveData<ItemAdapter>()
    var total = MutableLiveData<String>()

    private var state = MutableLiveData<InvoiceDetailState>()

    /**
     * Función que obtiene una factura a través de un id
     */
    fun getInvoiceById(id: Int): Invoice {
        return InvoiceProviderDB.getInvoiceById(id)
    }

    /**
     * Función que obtiene el cliente a través de su id
     */
    fun getCustomerById(customerId: CustomerId): Customer {
        return InvoiceProviderDB.getCustomerById(customerId)
    }

    /**
     * Función que obtiene una lista de items en base al id de la factura
     */
    fun getListItemByInvoiceId(idInvoice: InvoiceId): List<Item> {
        return InvoiceProviderDB.getListItem(idInvoice)
    }

    /**
     * Función que obtiene el total de una lista de items de una factura
     */
    fun giveTotal(lista: List<Item>): String {
        val suma = lista.sumOf { it.price }
        return String.format("%.2f€", suma)
    }

    /**
     * Función que borra de la base de datos, en primer lugar, de la tabla line_item las columnas
     * relacionadas con el id de la factura y en segundo lugar la factura, en este orden para
     * evitar errores de 'foreign key'
     */
    fun delete(invoice: Invoice) {
        val listaItems = LineItemProviderDB.getListItemsById(invoice.id.value)
        for (lineitem in listaItems) {
            LineItemProviderDB.delete(lineitem)
        }
        InvoiceProviderDB.delete(invoice)
    }

    fun onSuccess() {
        state.value = InvoiceDetailState.OnSuccess
    }

    fun getState(): LiveData<InvoiceDetailState> {
        return state
    }
}