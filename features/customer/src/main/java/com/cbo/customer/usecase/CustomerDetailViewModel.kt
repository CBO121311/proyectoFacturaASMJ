package com.cbo.customer.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cbo.customer.ui.CustomerDetailState
import com.moronlu18.data.customer.Customer
import com.moronlu18.repository.CustomerProvider

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
     * Borra el cliente desde el ViewModel.
     */
    fun deleteCustomer(customer: Customer) {
        val position = getPositionByCustomer(customer)
        if (position != -1) {
            repository.deleteCustomer(position)
        }
    }

    /**
     * Comprueba si es seguro borrar un cliente porque podría estar referenciado.
     * Devuelve true si no hay problema, false si lo hay.
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
     * Método que obtiene la posición del cliente en la lista
     */
    fun getPositionByCustomer(customer: Customer): Int {
        return repository.getPosByCustomer(customer)
    }

    /**
     * Obtiene el cliente en base a su posición en la lista.
     */
    fun getCustomerByPosition(posCustomer: Int): Customer {
        return repository.getCustomerPos(posCustomer)
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