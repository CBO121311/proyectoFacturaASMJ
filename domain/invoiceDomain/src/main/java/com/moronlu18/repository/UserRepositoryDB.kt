package com.moronlu18.repository

import android.database.sqlite.SQLiteException
import com.moronlu18.data.account.User
import com.moronlu18.database.InvoiceDatabase
import com.moronlu18.network.Resource
import kotlinx.coroutines.flow.Flow


class UserRepositoryDB {
    fun insert(user: User): Resource {
        try {
            InvoiceDatabase.getInstance()?.userDao()?.insert(user)
        } catch (e: SQLiteException) {
            return Resource.Error(e)
        }
        return Resource.Success(user)
    }

    fun getUserList(): Flow<List<User>> {
        return InvoiceDatabase.getInstance().userDao().selectAll()
    }

    fun delete(user: User) {
        InvoiceDatabase.getInstance().userDao().delete(user)
    }
}

