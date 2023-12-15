package com.cbo.customer.usecase

import android.util.Log
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
     * Función que pide el listado de customer al repositorio
     * Tiene una pantalla de carga
     * No tiene que devolver nada
     */

    fun getCustomerList() {
        viewModelScope.launch {
            state.value = CustomerListState.Loading(true)
            val result = CustomerProvider.getCustomerList()
            state.value = CustomerListState.Loading(false)
            Log.i("viewModel", "He pasado por getCustomerList")
            when (result) {
                is ResourceList.Success<*> -> state.value =
                    CustomerListState.Success(result.data as ArrayList<Customer>)

                is ResourceList.Error -> state.value = CustomerListState.NoDataError
            }
        }
    }

    /**
     * Función que pide el listado de customer al repositorio
     * No tiene tiempo de carga
     * Añadido por razones de exposición
     */

    fun getCustomerListNoLoading() {
        viewModelScope.launch {

            when (val result = CustomerProvider.getCustomerListNoLoading()) {
                is ResourceList.Success<*> -> state.value =
                    CustomerListState.Success(result.data as ArrayList<Customer>)

                is ResourceList.Error -> state.value = CustomerListState.NoDataError
            }
        }
    }

    /**
     * Obtiene el Customer en base a su posición de la lista.
     */

    fun getCustomerByPosition(posCustomer: Int): Customer {
        return repository.getCustomerPos(posCustomer)
    }

    /**
     * Comprueba si es seguro borrar un cliente porque podría estar referenciado.
     * Devuelve True si no hay problema, false si lo hay.
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
     * Ordena la lista de datos de clientes
     * en orden personalizado según el id.
     */
    fun sortRefresh(){
        repository.CustomerdataSet.sortBy { it.id }
    }

    /**
     *  Ordena la lista de datos de clientes
     *  en orden natural según el nombre.
     */
    fun sortName(){
        repository.CustomerdataSet.sort()
    }


    /**
     * Devuelve la variable State.
     * No se puede modificar su valor fuera de ViewModel.
     */
    fun getState(): LiveData<CustomerListState> {
        return state
    }
}