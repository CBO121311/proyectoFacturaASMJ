package com.moronlu18.account.usecase

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moronlu18.data.account.User
import com.moronlu18.data.account.UserSignUp
import com.moronlu18.network.Resource
import com.moronlu18.repository.UserRepository
import com.moronlu18.repository.UserRepositoryQuitar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.regex.Pattern

class SignUpViewModel : ViewModel() {


    private var state = MutableLiveData<SignUpState>()
    val name = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val password1 = MutableLiveData<String>()
    val password2 = MutableLiveData<String>()
    private val pattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")

    fun validateSignUp() {


        //UserRepositoryv2.selectAll()
        when {

            name.value.isNullOrBlank() -> state.value = SignUpState.NameEmptyError
            email.value.isNullOrBlank() -> state.value = SignUpState.EmailEmptyError
            !pattern.matcher(email.value!!).matches() -> state.value =
                SignUpState.EmailFormatError

            password1.value.isNullOrBlank() -> state.value = SignUpState.PasswordEmptyError
            password2.value.isNullOrBlank() -> state.value = SignUpState.PasswordEmptyError
            !isEqualPassword(password1.value!!, password2.value!!) -> state.value =
                SignUpState.PasswordNotEquals

            else -> {
                //postValue es asíncrono y no tenemos seguridad que lo haga.
                state.postValue(SignUpState.Loading(true))
                viewModelScope.launch(Dispatchers.IO) {


                    //para solucionar uno de los errores   withContext(Dispatchers.Main)
                    //utilizar el postValue que está dentro del liveData
                    //Que actualiza el hilo
                    //Todo Cambio de idea, se utiliza el contexto.


                    /*withContext(Dispatchers.Main){

                        state.value = SignUpState.Loading(true)
                    }*/

                    //state.postValue(SignUpState.Loading(true))
                    //Por qué post?, porque ya tengo un LiveData
                    //Y el procesador ya sabe donde ejecutar esta sentencia

                    //Log.i("viewModel", "He pasado por aquí")

                    //
                    val result = UserRepositoryQuitar.existEmailUser(
                        User(
                            name.value!!,
                            email.value!!
                        )
                    )



                    //Todo Lourdes añadir
                    //necesitamos el result.
                    //val result= userRepository.insert(user);


                    //Todo esto quiero que se haga antes
                    /*runBlocking {
                        state.postValue(SignUpState.Loading(false))
                    }*/


                    //UserRepository.insert(User("ABnnB","ABC"));



                    withContext(Dispatchers.Main){

                       state.value = SignUpState.Loading(false)
                    }




                    when (result) {

                        is Resource.Error -> {

                            withContext(Dispatchers.Main){
                                state.value = SignUpState.AuthencationError(result.exception.message!!)
                            }

                            //Todo Esta líenea es el que hay que añadir
                            //state.value = SignUpState.SignUpError(result.exception.message ?: "Unknown Error")

                            //state.value = SignUpState.AuthencationError(result.exception.message!!)

                            //state.postValue(SignUpState.AuthencationError(result.exception.message!!))
                        }


                        is Resource.Success<*> -> {

                            withContext(Dispatchers.Main){
                                state.value = SignUpState.OnSuccess(result.data as User)
                            }

                            //state.value = SignUpState.OnSuccess(result.data as User)
                            //state.postValue(SignUpState.OnSuccess(result.data as User))

                            //state.postValue(SignUpState.OnSuccess)
                        }
                    }
                }
            }
        }
    }

    private fun isEqualPassword(pass1: String, pass2: String): Boolean {
        return pass1 == pass2
    }

    fun getState(): LiveData<SignUpState> {
        return state
    }


    fun addUserSignUp(user: UserSignUp) {
        UserRepositoryQuitar.addUser(user.toUser())
    }

    fun addUserDirect(user: User) {
        UserRepositoryQuitar.addUser(user)
    }
}