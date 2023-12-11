package com.mto.invoice.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moronlu18.accounts.entity.Factura
import com.moronlu18.accounts.network.ResourceList
import com.moronlu18.accounts.repository.FacturaProvider
import kotlinx.coroutines.launch

//Tener una referencia al objeto eliminado por si se ejecuta un undo/control+z

class InvoiceListViewModel: ViewModel() {

    private var state = MutableLiveData<InvoiceListState>()
    private lateinit var invoiceDeleted: Factura

    fun getInvoiceList() {
        viewModelScope.launch {
            val result = FacturaProvider.getInvoiceList()
            when (result) {
                is ResourceList.Success<*> -> state.value =
                    InvoiceListState.Success(result.data as ArrayList<Factura>)
                is ResourceList.Error -> state.value = InvoiceListState.NoDataSet
            }
        }
    }

    fun getState() : LiveData<InvoiceListState> {
        return state
    }
}