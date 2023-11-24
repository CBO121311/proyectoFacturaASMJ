package com.moronlu18.account.usecase

import androidx.lifecycle.ViewModel
import com.moronlu18.accounts.repository.UserRepository

//import com.murray.invoice.data.repository.UserRepository


/*El que da los datos es el userCase.
Quien lo controla el dominio.

¿Qué es la base?
App es [main] está la interfaz principal.
Base - Para mostrar un mensaje de usuario- base --> classIU y fragmentDialog, es común a todos los usuarios.
Domain -Son los datos que comparten todos los módulos (?)
Features
*/

/**
 * El viewmodel comparten el ciclo de vida de los fragments
 *
 *     1º Vinculación de la IU viewModel
 *     2º Establecer la clase que mejorará las excepciones
 *     3º Iniciar la clase viewModel en la IU
 *     4º Implementar las funciones del ViewModel.
 *
    LiveData es que implementamos el observable.
    Si se actualiza el viewmodel, se actualiza en la vista automáticamente ?
    Los viewmodel son comunes a todos los fragments.

*/
class AccountUseCase: ViewModel () {
    fun getList(){
        //En base a las reglas de negocio
        UserRepository.dataSet;
    }
}