package com.moronlu18.accounts.network

import java.lang.Exception

//Clase de tipo de E como respuesta del servidor es settings

/**
 * Esta clase sellado representa la respuesta de un servicio API REST, firebase
 * donde se declara la clase Error que almacenara los errores de la infraestructura.
 * y el caso de éxito sea una colección de datos.
 */
sealed class Resource {
    //data class Success<T,E>(var data: T, var settings: E): Resource()
    //data class Success<T>(var data: Collection<T>): Resource() //List<String>, Set<User>, List<Account>
    data class Success<T>(var data: T?): Resource() //Si me conecto y hay éxito me da un data, sino un exception
    data class Error(var exception: Exception): Resource()

    //En la vista nunca se hace excepciones, sino un mensaje
}

//Tanto error como success puede tener varias funciones.


//Esto sería código duplicado. Más específico.
sealed class ResourceItem {
    data class Success<Item>(var data: Item?): Resource()
    data class Error(var exception: Exception): Resource()

}