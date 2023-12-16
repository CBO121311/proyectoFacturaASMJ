package com.moronlu18.accounts.network

import java.lang.Exception

/**
 * Esta clase sellado representa la respuesta de un servicio API REST, firebase
 * donde se declara la clase Error que almacenara los errores de la infraestructura.
 * y el caso de éxito sea una colección de datos.
 */
sealed class Resource {
    data class Success<T>(var data: T?) :
        Resource()
    data class Error(var exception: Exception) : Resource()
}