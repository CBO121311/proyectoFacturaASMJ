package com.cbo.customer.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cbo.customer.ui.CustomerDetailState
import com.cbo.customer.ui.CustomerListState
import com.moronlu18.accounts.entity.Customer
import com.moronlu18.accounts.repository.CustomerProvider

class CustomerDetailViewModel : ViewModel() {

    private var state = MutableLiveData<CustomerDetailState>()
    var idCustomer = MutableLiveData<String>()
    var nameCustomer = MutableLiveData<String>()
    var emailCustomer = MutableLiveData<String>()
    var phoneCustomer = MutableLiveData<String>()
    var addressCustomer = MutableLiveData<String>()
    var cityCustomer = MutableLiveData<String>()
    private val repository = CustomerProvider


    /**
     * Método que obtiene la posición del Customer en base al Customer en si.
     */
    private fun getPositionById(customer: Customer): Int {
        return repository.getPosByCustomer(customer)
    }


    /**
     * Borra el cliente desde el viewModel
     */
    fun deleteCustomer(customer: Customer) {
        val position = getPositionById(customer)
        if (position != -1) {
            repository.deleteCustomer(position)
        }
    }

    /**
     * Comprueba si es seguro borrar un cliente porque podría estar referenciado.
     * Devuelve True si no hay problema, false si lo hay.
     */
    fun isDeleteSafe(customer: Customer): Boolean {
        return if (repository.isCustomerSafeDelete(customer.id)) {
            state.value = CustomerDetailState.ReferencedCustomer
            false
        } else {
            true
        }
    }

    /**
     * Método que da el estado "onSuccess" en el contexto
     * de la customerDetail.
     */
    fun onSuccess() {
        state.value = CustomerDetailState.OnSuccess
    }

    /**
     * Devuelve la variable State.
     * No se puede modificar su valor fuera de ViewModel.
     */
    fun getState(): LiveData<CustomerDetailState> {
        return state
    }
}