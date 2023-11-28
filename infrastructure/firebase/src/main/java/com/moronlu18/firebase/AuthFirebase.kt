package com.moronlu18.firebase

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.moronlu18.accounts.entity.Account
import com.moronlu18.accounts.entity.AccountState
import com.moronlu18.accounts.entity.Email
import com.moronlu18.accounts.network.Resource
//import com.moronlu18.accounts.network.Resource



import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AuthFirebase {
    private var authFirebase = FirebaseAuth.getInstance()

    suspend fun login (email:String, password: String): Resource {
        //Este  código se ejecuta en un hilo especifico para operaciones entrada/salida (IO)
        withContext(Dispatchers.IO){
            delay(3000)
            //Se ejecutara el codigo de consulta a FireBase. Que puede tardar más de 5s y en ese
            //caso se obtiene el error ANR (Android Not Responding) porque puede bloquear la vista
            try{
                val authResult : AuthResult = authFirebase.signInWithEmailAndPassword(email,password).await()
            val user = authResult.user
            //val account :Account = Account.create(us!!.uid.toInt(),user.email, password, null, AccountState.UNVERIFIED)

//ght123456@hotmail.es
            //Resource.Success(account)
            }catch (e: Exception){
                Resource.Error(e)
            }
        }
        return Resource.Error(Exception("El password es incorrecto"))
    }
}



