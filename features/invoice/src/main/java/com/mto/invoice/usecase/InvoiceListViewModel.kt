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

class InvoiceListViewModel : ViewModel() {

    private var state = MutableLiveData<InvoiceListState>()
    private lateinit var invoiceDeleted: Factura

    /**
     * Función que pide el listado con una pantalla de carga
     */
    fun getInvoiceList() {
        viewModelScope.launch {
            state.value = InvoiceListState.Loading(true)
            val result = FacturaProvider.getInvoiceList()
            state.value = InvoiceListState.Loading(false)

            when (result) {
                is ResourceList.Success<*> -> {
                    val facturas = result.data as ArrayList<Factura>
                    facturas.sortBy { it.id }
                    state.value = InvoiceListState.Success(facturas)
                }


                is ResourceList.Error -> state.value = InvoiceListState.NoDataSet
            }
        }
    }

    /**
     * Función que devuelve la lista sin carga llamada cuando se borra una factura
     */
    fun getListWithoutLoading() {
        viewModelScope.launch {

            when (val result = FacturaProvider.getListWithoutLoading()) {
                is ResourceList.Success<*> -> {
                    val facturas = result.data as ArrayList<Factura>
                    facturas.sortBy { it.id }
                    state.value = InvoiceListState.Success(facturas)
                }

                is ResourceList.Error -> state.value = InvoiceListState.NoDataSet
            }
        }
    }

    fun getPosByInvoice(factura: Factura): Int {
        return FacturaProvider.getPosByInvoice(factura)
    }

    /**
     * Función que devuelve la variable state
     */
    fun getState(): LiveData<InvoiceListState> {
        return state
    }
}