package com.moronlu18.accounts.network

import java.lang.Exception

sealed class ResourceList {
    data class Success<T>(var data: ArrayList<T>?): ResourceList() //Si me conecto y hay éxito me da un data, sino un exception
    data class Error(var exception: Exception): ResourceList()
}