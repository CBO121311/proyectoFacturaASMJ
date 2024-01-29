package com.moronlu18.repository

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import com.moronlu18.data.account.User
import com.moronlu18.database.InvoiceDatabase
import com.moronlu18.network.Resource
import kotlinx.coroutines.flow.Flow


class UserRepository {

    /*fun insert(user: User): UserState {
        return try {
            InvoiceDatabase.getInstance()?.userDao()?.insert(user)
            UserState.Success
        } catch (ex: SQLiteConstraintException) {
            UserState.InsertError("Error")
        }
    }*/
companion object{

        fun insert(user: User): Resource {
            try {
                InvoiceDatabase.getInstance()?.userDao()?.insert(user)
            } catch (e:SQLiteException){
                return Resource.Error(e)
            }
            return Resource.Success(user)
        }

    }

    fun getUserList(): Flow<List<User>> {
        return InvoiceDatabase.getInstance().userDao().selectAll()
    }

    fun delete(user: User) {
        InvoiceDatabase.getInstance().userDao().delete(user)
    }

    //Un select es un join de invoice con line_item es un mapa.
}

