package com.moronlu18.data.account

import com.moronlu18.data.base.UniqueId


//es de la base de datos a mi modelo.
data class AccountId(override val value: Int): UniqueId(value) {
}