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

    fun validateCredentials() {
        Log.i(
            TAG,
            "El email es: ${emailCustomer.value} y el nombre es ${nameCustomer.value} y ${idCustomer.value}"
        )

        Log.i(
            TAG,
            "El editor está ${isEditor.value} y el previo customer existe $prevCustomer"
        )
        // El viewModel comprueba todas las excepcicones
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


    fun addCustomer(customer: Customer) {
        repository.addOrUpdateCustomer(customer, null)
    }

    fun updateCustomer(customer: Customer, posCustomer: Int) {
        repository.addOrUpdateCustomer(customer, posCustomer)
    }

    fun setEditorMode(isEditorMode: Boolean) {
        isEditor.value = isEditorMode
    }

    fun getEditorMode(): Boolean {
        return isEditor.value ?: false
    }


    fun getNextCustomerId(): Int {
        val maxId = repository.getMaxCustomerid()

        println("EL MAXIMO ID ES $maxId")
        return maxId + 1
    }


    fun getCustomerByPosition(posCustomer: Int): Customer {
        return repository.getCustomerPos(posCustomer)
    }

    /**
     * Se crea solo la función de obtención de la variable State.
     * No se puede modificar su valor fuera de ViewModel.
     */
    fun getState(): LiveData<CustomerCreationState> {
        return state
    }
}