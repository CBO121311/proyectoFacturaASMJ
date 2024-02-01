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
import com.moronlu18.repository.CustomerProviderDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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
            //state.value = CustomerListState.Loading(true)
            //val result = CustomerProvider.getCustomerList()
            //state.value = CustomerListState.Loading(false)

            when{
                allCustomer.value?.isEmpty() == true -> state.value = CustomerListState.NoDataError
                else -> state.value = CustomerListState.Success
            }

           /* when (result) {
                is ResourceList.Success<*> -> {
                    /*val list = result.data as ArrayList<Customer>

                    val sortPreference = Locator.settingsPreferencesRepository.getSettingValue("customersort","id")
                    when(sortPreference){
                        "id" -> list.sortBy { it.id }
                        "name_asc" -> list.sortBy { it.name }
                        "name_desc" -> list.sortByDescending { it.name }
                        "email" -> list.sortBy { it.email.toString() }
                    }*/


                    state.value = CustomerListState.Success
                }

                is ResourceList.Error -> state.value = CustomerListState.NoDataError
            }*/
        }
    }

    /**
     * Función que pide el listado de customer al repositorio sin tiempo de carga.
     * Añadido por razones de exposición.
     */
    fun getCustomerListNoLoading() {
        viewModelScope.launch {

            when (val result = CustomerProvider.getCustomerListNoLoading()) {
                is ResourceList.Success<*> -> {
                    val list = result.data as ArrayList<Customer>

                    val sortPreference = Locator.settingsPreferencesRepository.getSettingValue("customersort","id")
                    when(sortPreference){
                        "id" -> list.sortBy { it.id }
                        "name_asc" -> list.sortBy { it.name }
                        "name_desc" -> list.sortByDescending { it.name }
                        "email" -> list.sortBy { it.email.toString() }
                    }

                    state.value = CustomerListState.Success
                }

                is ResourceList.Error -> state.value = CustomerListState.NoDataError
            }
        }
    }


    /**
     * Comprueba si es seguro borrar un cliente porque podría estar referenciado.
     * Devuelve true si no hay problema, false si lo hay.
     */
    fun isDeleteSafe(customer: Customer): Boolean {
        return  true
       /* return if (customerProviderDB.isCustomerSafeDelete(customer.id.value)) {
            state.value = CustomerListState.ReferencedCustomer
            false
        } else {
            true
        }*/
    }

    fun delete (customer: Customer){
        viewModelScope.launch(Dispatchers.IO) {
            customerProviderDB.delete(customer)
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