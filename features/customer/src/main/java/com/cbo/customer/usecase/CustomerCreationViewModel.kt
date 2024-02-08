package com.cbo.customer.usecase


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cbo.customer.ui.CustomerCreationState
import com.moronlu18.data.customer.Customer
import com.moronlu18.repository.CustomerProviderDB
import java.util.regex.Pattern


class CustomerViewModel : ViewModel() {

    var nameCustomer = MutableLiveData<String>()
    var emailCustomer = MutableLiveData<String>()

    private var state = MutableLiveData<CustomerCreationState>()

    private val pattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
    private val customerProviderDB = CustomerProviderDB()

    /**
     * Método que valida distintos estados al comprobar si los datos están escritos correctamente.
     */
    fun validateCredentials() {

        when {
            nameCustomer.value.isNullOrBlank() -> state.value =
                CustomerCreationState.NameIsMandatory

            emailCustomer.value.isNullOrBlank() -> state.value =
                CustomerCreationState.EmailEmptyError

            !pattern.matcher(emailCustomer.value.toString()).matches() -> state.value =
                CustomerCreationState.InvalidEmailFormat

            else -> {
                state.value = CustomerCreationState.OnSuccess
            }
        }
    }

    /**
     * Añade un cliente.
     */
    fun addCustomer(customer: Customer) {

        customerProviderDB.insert(customer)
    }

    /**
     * Actualiza un cliente.
     */
    fun updateCustomer(customer: Customer) {
        customerProviderDB.update(customer)
    }

    /**
     * Devuelve el siguiente ID autogenerado, que es el máximo ID de la tabla + 1.
     */
    fun getNextCustomerId(): Int {
        return 1 + (customerProviderDB.getLastCustomerId() ?: 0)
    }

    /**
     * Devuelve la variable State.
     * No se puede modificar su valor fuera del ViewModel.
     */
    fun getState(): LiveData<CustomerCreationState> {
        return state
    }
}