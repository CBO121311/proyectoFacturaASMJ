package com.cbo.customer.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cbo.customer.ui.CustomerDetailState
import com.moronlu18.data.customer.Customer
import com.moronlu18.network.Resource
import com.moronlu18.repository.CustomerProviderDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CustomerDetailViewModel : ViewModel() {

    private var state = MutableLiveData<CustomerDetailState>()
    var idCustomer = MutableLiveData<String>()
    var nameCustomer = MutableLiveData<String>()
    var emailCustomer = MutableLiveData<String>()
    var phoneCustomer = MutableLiveData<String>()
    var addressCustomer = MutableLiveData<String>()
    var cityCustomer = MutableLiveData<String>()
    private val customerProviderDB = CustomerProviderDB()


    fun delete(customer: Customer) {
        viewModelScope.launch(Dispatchers.IO) {

            when (customerProviderDB.delete(customer)) {
                is Resource.Success<*> -> {}

                else -> {
                    state.value = CustomerDetailState.ReferencedCustomer
                }
            }
        }
    }

    /**
     * Método que establece el estado "onSuccess" en el contexto de CustomerDetail.
     */
    fun onSuccess() {
        state.value = CustomerDetailState.OnSuccess
    }

    /**
     * Devuelve la variable State. No se puede modificar su valor fuera del ViewModel.
     */
    fun getState(): LiveData<CustomerDetailState> {
        return state
    }
}