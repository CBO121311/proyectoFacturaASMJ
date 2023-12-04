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

    fun getCustomerListNoLoading() {
        viewModelScope.launch {

            when (val result = CustomerProvider.getCustomerListNoLoading()) {
                is ResourceList.Success<*> -> state.value =
                    CustomerListState.Success(result.data as ArrayList<Customer>)

                is ResourceList.Error -> state.value = CustomerListState.NoDataError
            }
        }
    }


    /*fun refreshCustomerList() {
        viewModelScope.launch {
            when (val result = CustomerProvider.getCustomerList()) {
                is ResourceList.Success<*> -> state.value =
                    CustomerListState.Success(result.data as ArrayList<Customer>)

                is ResourceList.Error -> state.value = CustomerListState.NoDataError
            }
        }
    }*/

    fun getCustomerByPosition(posCustomer: Int): Customer {
        return repository.getCustomerPos(posCustomer)
    }
    fun isDeleteSafe(customer: Customer): Boolean {
        val isSafe = repository.isCustomerSafeDelete(customer.id)
        if (isSafe) {
            state.value = CustomerListState.ReferencedCustomer
        }
        return isSafe
    }

    fun getState(): LiveData<CustomerListState> {
        return state
    }
}