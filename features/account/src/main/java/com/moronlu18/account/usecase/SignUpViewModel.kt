package com.moronlu18.account.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moronlu18.data.account.User
import com.moronlu18.network.Resource
import com.moronlu18.repository.UserRepositoryDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.regex.Pattern

class SignUpViewModel : ViewModel() {


    private var state = MutableLiveData<SignUpState>()
    val name = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val password1 = MutableLiveData<String>()
    val password2 = MutableLiveData<String>()
    private val pattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
    private var userRepositoryDB = UserRepositoryDB()
    fun validateSignUp() {


        //UserRepositoryv2.selectAll()
        when {

            name.value.isNullOrBlank() -> state.value = SignUpState.NameEmptyError
            email.value.isNullOrBlank() -> state.value = SignUpState.EmailEmptyError
            !pattern.matcher(email.value!!).matches() -> state.value = SignUpState.EmailFormatError
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

                    //
                    /*val result = UserRepositoryQuitar.existEmailUser(
                        User(
                            name.value!!,
                            email.value!!
                        )
                    )*/

                    val user = User(name.value!!,email.value!!)

                    val result = userRepositoryDB.insert(user);

                    withContext(Dispatchers.Main) {
                        state.value = SignUpState.Loading(false)
                    }

                    when (result) {
                        is Resource.Error -> {

                            withContext(Dispatchers.Main) {
                                state.value =
                                    SignUpState.AuthencationError(result.exception.message!!)
                            }
                        }

                        is Resource.Success<*> -> {

                            withContext(Dispatchers.Main) {
                                state.value = SignUpState.OnSuccess(result.data as User)
                            }
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


    /*fun addUserSignUp(user: UserSignUp) {
        UserRepositoryQuitar.addUser(user.toUser())
    }
    fun addUserDirect(user: User) {
        UserRepositoryQuitar.addUser(user)
    }*/

}