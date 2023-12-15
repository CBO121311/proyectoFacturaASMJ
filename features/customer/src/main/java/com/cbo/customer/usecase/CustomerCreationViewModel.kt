package com.cbo.customer.usecase

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cbo.customer.ui.CustomerCreationState
import com.moronlu18.accounts.entity.Customer
import com.moronlu18.accounts.repository.CustomerProvider
import java.util.regex.Pattern

const val TAG = "ViewModel"

class CustomerViewModel : ViewModel() {

    var idCustomer = MutableLiveData<String>()
    var prevCustomer: Customer? = null
    private var isEditor = MutableLiveData<Boolean>()
    var nameCustomer = MutableLiveData<String>()
    var emailCustomer = MutableLiveData<String>()

    private var state = MutableLiveData<CustomerCreationState>()

    private val pattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
    private val repository = CustomerProvider



    /**
     * Método que da distintos estados al comprobar si los datos está
     * escrito de forma correcta.
     */
    fun validateCredentials() {
        Log.i(
            TAG,
            "El email es: ${emailCustomer.value} y el nombre es ${nameCustomer.value} y ${idCustomer.value}"
        )

        when {
            TextUtils.isEmpty(nameCustomer.value) -> state.value =
                CustomerCreationState.NameIsMandatory

            TextUtils.isEmpty(emailCustomer.value) -> state.value =
                CustomerCreationState.EmailEmptyError

            !pattern.matcher(emailCustomer.value.toString()).matches() -> state.value =
                CustomerCreationState.InvalidEmailFormat

            isEditor.value ?: false && (CustomerProvider.isCustomerExistOrNumeric(
                idCustomer.value!!,
                prevCustomer!!
            )) -> state.value =
                CustomerCreationState.InvalidId

            else -> {
                state.value = CustomerCreationState.OnSuccess
            }
        }
    }

    /**
     * Añade un cliente al repositorio
     */
    fun addCustomer(customer: Customer) {
        repository.addOrUpdateCustomer(customer, null)
    }

    /**
     * Actualiza un cliente al repositorio.
     * Esto solo se hace en el modo editar
     */
    fun updateCustomer(customer: Customer, posCustomer: Int) {
        repository.addOrUpdateCustomer(customer, posCustomer)
    }

    /**
     * Cambia el estado de editar.
     */
    fun setEditorMode(isEditorMode: Boolean) {
        isEditor.value = isEditorMode
    }

    /**
     * Devuelve si actualmente está el modo editar.
     * Si es nulo devuelve false.
     */
    fun getEditorMode(): Boolean {
        return isEditor.value ?: false
    }

    /**
     * Devuelve el siguiente Id autogenerado, que es el máximo id +1
     * Esto se utiliza en el modo crear, no el de editar.
     */
    fun getNextCustomerId(): Int {
        val maxId = repository.getMaxCustomerid()

        return maxId + 1
    }

    /**
     * Obtiene el Customer en base a su posición de la lista.
     * Se utiliza para editar el cliente
     */
    fun getCustomerByPosition(posCustomer: Int): Customer {
        return repository.getCustomerPos(posCustomer)
    }

    fun sortRefresh(){
        repository.CustomerdataSet.sortBy { it.id }
    }

    /**
     * Devuelve la variable State.
     * No se puede modificar su valor fuera de ViewModel.
     */
    fun getState(): LiveData<CustomerCreationState> {
        return state
    }
}