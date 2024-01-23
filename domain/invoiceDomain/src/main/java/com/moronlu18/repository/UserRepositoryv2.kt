package com.moronlu18.repository

import com.moronlu18.data.account.User
import com.moronlu18.database.InvoiceDatabase

class UserRepositoryv2 {
    fun insert(user:User){
        InvoiceDatabase.getInstance()?.userDao()?.insert(user)
    }
}