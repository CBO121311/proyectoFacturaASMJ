package com.moronlu18.account.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moronlu18.data.account.User
import com.moronlu18.network.ResourceList
import com.moronlu18.repository.UserRepository
import kotlinx.coroutines.launch

class UserListViewModel : ViewModel() {

    private var state = MutableLiveData<UserListState>()

    //    private var userRepository
//    val result = userRepository.get
    //result userRepository.getUserList().asLiveData
    //result.value.isEmpty() == true ->


    //Los flow está dando  una secuencia de valores de User. pero puede tener un error en el tema del Resource.

    /*fun getUser():Flow<List<User>>{

        //InvoiceDatabase.getInstance()?.userDao?.All
    }*/

    //ForeignKey(entity = User::class, parent)


    fun getState(): LiveData<UserListState> {
        return state
    }

    /**
     * Función que pide el listado de usuarios al repositorio
     * No tiene que devolver nada
     */
    fun getUserList() {
        //Iniciar la corrutina
        viewModelScope.launch {
            state.value = UserListState.Loading(true)

            val result = UserRepository.getUserList()

            state.value = UserListState.Loading(false)

            when (result) {
                is ResourceList.Success<*> -> {
                    state.value = UserListState.Success(result.data as ArrayList<User>)
                }
                is ResourceList.Error -> state.value = UserListState.NoDataError
                else -> {}
            }
        }
    }

    fun sortNatural() {
        UserRepository.dataSet.sort()
    }

    fun sortPreestablecido() {
        UserRepository.dataSet.sortBy { it.email.toString().lowercase() }
    }
}