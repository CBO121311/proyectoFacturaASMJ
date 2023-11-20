package com.moronlu18.accountsignin.usecase

import androidx.lifecycle.ViewModel
import com.moronlu18.accountsignin.data.repository.UserRepository

//import com.murray.invoice.data.repository.UserRepository


//El que da los datos es el userCase.
//Quien lo controla el dominio.

//¿Qué es la base?
//App es [main] está la interfaz principal.
//Base - Para mostrar un mensaje de usuario- base --> classIU y fragmentDialog, es común a todos los usuarios.
//Domain -Son los datos que comparten todos los módulos (?)
//Features
class AccountUseCase: ViewModel () {
    fun getList(){
        //En base a las reglas de negocio
        UserRepository.dataSet;
    }
}