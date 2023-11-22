package com.moronlu18.account.ui.signin

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

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

  fun validateCredentials(){
    Log.i(TAG,"El email es: ${email.value} y el password es ${password.value}")
    email.value = "nuevo valor"

//El viewModel comprueba todas las excepcicones
    when{
      TextUtils.isEmpty(email.value) -> state.value = SignInState.EmailEmptyError
      TextUtils.isEmpty(password.value) -> state.value = SignInState.PasswordEmptyError
      else -> state.value = SignInState.Success
    }

  }


  /**
   * Se crea solo la función de obtención de la variable State.
   * No se puede modificar su valor fuera de ViewModel.
   */
  fun getState(): LiveData<SignInState>{
    return state
  }
}