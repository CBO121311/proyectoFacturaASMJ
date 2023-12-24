package com.cbo.customer.usecase


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbo.customer.ui.CustomerListState
import com.moronlu18.accounts.entity.Customer
import com.moronlu18.accounts.network.ResourceList
import com.moronlu18.accounts.repository.CustomerProvider
import kotlinx.coroutines.launch

class CustomerListViewModel : ViewModel() {

    private var state = MutableLiveData<CustomerListState>()
    private val repository = CustomerProvider


    /**
     * Función que pide el listado de customer al repositorio con una pantalla de carga.
     * No tiene que devolver nada.
     */
    fun getCustomerList() {
        viewModelScope.launch {
            state.value = CustomerListState.Loading(true)
            val result = CustomerProvider.getCustomerList()
            state.value = CustomerListState.Loading(false)

            when (result) {
                is ResourceList.Success<*> -> {
                    val lista = result.data as ArrayList<Customer>
                    lista.sortBy { it.id }
                    state.value = CustomerListState.Success(lista)
                }

                is ResourceList.Error -> state.value = CustomerListState.NoDataError
            }
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
                    val lista = result.data as ArrayList<Customer>
                    lista.sortBy { it.id }
                    state.value = CustomerListState.Success(lista)
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
        return if (repository.isCustomerSafeDelete(customer.id)) {
            state.value = CustomerListState.ReferencedCustomer
            false
        } else {
            true
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