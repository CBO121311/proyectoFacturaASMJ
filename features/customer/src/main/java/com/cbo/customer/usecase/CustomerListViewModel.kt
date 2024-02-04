package com.cbo.customer.usecase


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.asLiveData
import com.cbo.customer.ui.CustomerListState
import com.moronlu18.data.customer.Customer
import com.moronlu18.network.ResourceList
import com.moronlu18.repository.CustomerProvider
import com.moronlu18.invoice.Locator
import com.moronlu18.network.Resource
import com.moronlu18.repository.CustomerProviderDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CustomerListViewModel : ViewModel() {

    private var state = MutableLiveData<CustomerListState>()
    private var customerProviderDB = CustomerProviderDB()
    var allCustomer = customerProviderDB.getCustomerList().asLiveData()

    /**
     * Función que pide el listado de customer al repositorio con una pantalla de carga.
     * No tiene que devolver nada.
     */
    fun getCustomerList() {
        viewModelScope.launch {

            when {
                allCustomer.value?.isEmpty() == true -> state.value = CustomerListState.NoDataError

                else -> {
                    allCustomer = when (Locator.settingsPreferencesRepository.getSettingValue("customersort", "id")) {
                        "id" -> customerProviderDB.getCustomerList().asLiveData()
                        "name_asc" -> customerProviderDB.getCustomerListName().asLiveData()
                        "name_desc" -> customerProviderDB.getCustomerListNameDesc().asLiveData()
                        "email" -> customerProviderDB.getCustomerListEmail().asLiveData()
                        else -> customerProviderDB.getCustomerList().asLiveData()
                    }

                    state.value = CustomerListState.Success
                }
            }
        }
    }

    fun delete(customer: Customer) {
        viewModelScope.launch(Dispatchers.IO) {

            when (customerProviderDB.delete(customer)) {
                is Resource.Success<*> -> {}

                is Resource.Error -> {
                    withContext(Dispatchers.Main) {
                        state.value = CustomerListState.ReferencedCustomer
                    }
                }
            }
        }
    }

    /**
     * Devuelve la variable State.
     * No se puede modificar su valor fuera del ViewModel.
     */
    fun getState(): LiveData<CustomerListState> {
        return state
    }
}