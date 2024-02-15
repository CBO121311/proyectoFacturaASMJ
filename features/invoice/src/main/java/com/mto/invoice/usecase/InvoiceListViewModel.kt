package com.mto.invoice.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.moronlu18.data.invoice.Invoice

import com.moronlu18.invoice.Locator
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

                else -> {
                    allInvoice = when (Locator.settingsPreferencesRepository.getSettingValue(
                        "invoicesort",
                        "id"
                    )) {
                        "id" -> InvoiceProviderDB.getInvoiceListFlow().asLiveData()
                        "name_asc" -> InvoiceProviderDB.getInvoiceByNameAZ().asLiveData()
                        "name_desc" -> InvoiceProviderDB.getInvoiceByNameZA().asLiveData()
                        "status" -> InvoiceProviderDB.getInvoiceByStatus().asLiveData()
                        else -> InvoiceProviderDB.getInvoiceListFlow().asLiveData()
                    }

                    state.value = InvoiceListState.Success
                }
            }

        }
    }


    /**
     * Función que devuelve la variable state
     */
    fun getState(): LiveData<InvoiceListState> {
        return state
    }

}