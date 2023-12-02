package com.cbo.customer.usecase

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cbo.customer.ui.CustomerState
import java.util.regex.Pattern

const val TAG = "ViewModel"

class CustomerViewModel : ViewModel() {

    var nameCustomer = MutableLiveData<String>()
    var emailCustomer = MutableLiveData<String>()

    private var state = MutableLiveData<CustomerState>()

    private val pattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")


//Crear la clase sellada que permitirá gestionar las excepciones de la vista.

    /**
     * Esta función se ejecuta directamente desde el fichero xml al user
     *  DataBinding android:onClick="@{()->viewmodel.validateCredentials()}"
     */

/*    suspend fun deleteCusto(customer: Customer): Boolean {



        return CustomerProvider.deleteCustomer(customer)
    }*/


    fun validateCredentials() {
        Log.i(TAG, "El email es: ${emailCustomer.value} y el nombre es ${nameCustomer.value}")
        // El viewModel comprueba todas las excepcicones
        when {
            TextUtils.isEmpty(nameCustomer.value) -> state.value = CustomerState.NameIsMandatory
            TextUtils.isEmpty(emailCustomer.value) -> state.value = CustomerState.EmailEmptyError
            !pattern.matcher(emailCustomer.value).matches() -> state.value = CustomerState.InvalidEmailFormat
            else->{
                state.value = CustomerState.OnSuccess
            }

        }
    }

    /**
     * Se crea solo la función de obtención de la variable State.
     * No se puede modificar su valor fuera de ViewModel.
     */
    fun getState(): LiveData<CustomerState> {
        return state
    }
}