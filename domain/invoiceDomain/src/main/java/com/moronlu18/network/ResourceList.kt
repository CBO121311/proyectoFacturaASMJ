package com.moronlu18.network

import java.lang.Exception

sealed class ResourceList {
    data class Success<T>(var data: ArrayList<T>?): ResourceList() //Si me conecto y hay éxito me da un data, si no un exception
    data class Error(var exception: Exception): ResourceList()
}