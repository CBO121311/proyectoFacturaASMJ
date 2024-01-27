package com.moronlu18.repository

import com.moronlu18.data.account.User
import com.moronlu18.database.InvoiceDatabase
import kotlinx.coroutines.flow.Flow



class UserRepository {
    companion object{
        fun insert(user:User){
            InvoiceDatabase.getInstance()?.userDao()?.insert(user)
        }
//inserterror
        //success


        /*
        fun insertv(user:User):UserState{
            try{

InvoiceDatabase.getInstance()?.userDao()?.insert(user)
            }catch ()

        }*/
    }

    fun getUserList():Flow<List<User>>
    {
        return InvoiceDatabase.getInstance().userDao().selectAll()
    }

    fun delete(user: User){
        //InvoiceDatabase.getInstance().userDao().delete(user)
    }

    //Un select es un join de invoice con line_item es un mapa.
}

