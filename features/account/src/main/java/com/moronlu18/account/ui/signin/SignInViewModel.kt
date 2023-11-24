package com.moronlu18.account.ui.signin

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moronlu18.accounts.network.Resource
import com.moronlu18.accounts.repository.UserRepository
import kotlinx.coroutines.launch

const val TAG = "ViewModel"

class SignInViewModel : ViewModel() {
    //LiveData que controlan los datos introducidos en la IU
    var email = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    //LiveData que tendrá su Observador en el Fragment y controla las excepciones/casos de uso de la operación Login.
    //Se hace privado, ya que no quiero que un fragment cambie esa propiedad


    private var state = MutableLiveData<SignInState>()

//Crear la clase sellada que permitirá gestionar las excepciones de la vista.

    /**
     * Esta función se ejecuta directamente desde el fichero xml al user
     *  DataBinding android:onClick="@{()->viewmodel.validateCredentials()}"
     */

    fun validateCredentials() {
        Log.i(TAG, "El email es: ${email.value} y el password es ${password.value}")
        //email.value = "nuevo valor"
        //password.value = "123"
        // El viewModel comprueba todas las excepcicones
        when {
            TextUtils.isEmpty(email.value) -> state.value = SignInState.EmailEmptyError
            TextUtils.isEmpty(password.value) -> state.value = SignInState.PasswordEmptyError
            //EmailFormat
            //PasswordFormat


            else -> {
                /*Se crea una corrutina que suspende el hilo principal hasta que el
                bloque with Context el repositorio termina de ejecutarse.
                */

                viewModelScope.launch {
                    /*
                    Vamos a ejecutar el Login del repositorio -> que pregunta a la capa de la infrasestructura
                    val result = UserRepository.login(email.value!!,password.value!!) //esto lo hace el repositorio.
                    Esto me devuelve un Resource, es decir un data Class.
                    is cuando sea un data class
                    */
                    when (val result = UserRepository.login(
                        email.value!!,
                        password.value!!
                    )) {
                        //esto es una clase sellada (Resource)
                        is Resource.Success<*> -> {
                            //Aquí tenemos que hacer un Casting Seguro porque el tipo de dato es genérico T.
                            Log.i(TAG, "Información del dato ${result.data}")

                        }

                        is Resource.Error -> {
                            Log.i(TAG, "Información del dato ${result.exception.message}")
                            state.value = SignInState.AuthencationError(result.exception.message!!)
                        }
                    }
                }
            }
        }

    }


    /**
     * Se crea solo la función de obtención de la variable State.
     * No se puede modificar su valor fuera de ViewModel.
     */
    fun getState(): LiveData<SignInState> {
        return state
    }
}