package com.moronlu18.account.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moronlu18.accounts.entity.User
import com.moronlu18.accounts.network.ResourceList
import com.moronlu18.accounts.repository.UserRepository
import kotlinx.coroutines.launch

class UserListViewModel : ViewModel() {

    private var state = MutableLiveData<UserListState>()

    //El liveData solo puedes objetenerlo el usuario.  Es privado
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

            //Opción 1: me devuelve diferentes estados
            //Este resourceList si tiene más de un error.

            val result = UserRepository.getUserList()

            state.value = UserListState.Loading(false)

            when (result) {
                is ResourceList.Success<*> -> {
                    state.value = UserListState.Success(result.data as ArrayList<User>)
                }

                is ResourceList.Error -> state.value = UserListState.NoDataError
            }

            //Opción 2 : me devuelve la lista, porque solo tenemos 2 posibles estados
            // el de error y el de exito
            //Esto no es lo normal.
            /* val data = UserRepository.getUserList()
             when{
                 data.isEmpty() -> state.value = UserListState.NoDataError
                 else -> state.value = UserListState.Success(data)
             }*/

            //Lo único que difiere es el estado del error.

            //state.value = UserListState.Loading(false)
        }
    }

    fun sortNatural() {
        //Orden personalizado se estable mediante una propiedad (no es orden natural)
        UserRepository.dataSet.sort()

    }

    fun sortPreestablecido() {
        UserRepository.dataSet.sortBy { it.email }

    }

}