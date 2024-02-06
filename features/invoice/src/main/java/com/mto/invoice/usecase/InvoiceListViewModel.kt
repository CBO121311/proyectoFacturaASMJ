package com.mto.invoice.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.moronlu18.data.invoice.Invoice
import com.moronlu18.network.ResourceList
import com.moronlu18.repository.InvoiceProvider
import com.moronlu18.invoice.Locator
import com.moronlu18.repository.CustomerProvider
import com.moronlu18.repository.InvoiceProviderDB
import kotlinx.coroutines.launch

//Tener una referencia al objeto eliminado por si se ejecuta un undo/control+z

class InvoiceListViewModel : ViewModel() {

    private var state = MutableLiveData<InvoiceListState>()
    private lateinit var invoiceDeleted: Invoice
    var allInvoice = InvoiceProviderDB.getInvoiceListFlow().asLiveData()

    /**
     * Función que pide el listado con una pantalla de carga
     */

    fun getInvoiceList() {
        viewModelScope.launch {
            when {
                allInvoice.value?.isEmpty() == true -> state.value = InvoiceListState.NoDataSet
                else -> state.value = InvoiceListState.Success
            }

          /*  val sortPreference = Locator.settingsPreferencesRepository.getSortInvoice()
            when (sortPreference) {
                "id" -> lista.sortBy { it.id }
                "name_asc" -> lista.sortBy { getCustomerName(it.customerId.value) }
                "name_desc" -> lista.sortByDescending { getCustomerName(it.customerId.value) }
                "status" -> lista.sortBy { it.status.toString() }
            }
            state.value = InvoiceListState.Success*/

        }
    }

    /**
     * Función que devuelve la lista sin carga llamada cuando se borra una factura
     */
    fun getListWithoutLoading() {
        viewModelScope.launch {

            when (val result = InvoiceProvider.getListWithoutLoading()) {
                is ResourceList.Success<*> -> {
                    val invoices = result.data as ArrayList<Invoice>
                    val sortPreference = Locator.settingsPreferencesRepository.getSortInvoice()


                    when (sortPreference) {
                        "id" -> invoices.sortBy { it.id }
                        "name_asc" -> invoices.sortBy { getCustomerName(it.customerId.value) }
                        "name_desc" -> invoices.sortByDescending { getCustomerName(it.customerId.value) }
                        "status" -> invoices.sortBy { it.status.toString() }
                    }
                    state.value = InvoiceListState.Success
                }

                is ResourceList.Error -> state.value = InvoiceListState.NoDataSet
                else -> {}
            }
        }
    }

    /**
     * Función que devuelve la variable state
     */
    fun getState(): LiveData<InvoiceListState> {
        return state
    }

    private fun getCustomerName(customerId: Int): String? {
        return CustomerProvider.getCustomerNameById(customerId)
    }
}