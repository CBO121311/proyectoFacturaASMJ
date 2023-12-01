package com.moronlu18.firebase

import android.util.Log
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.moronlu18.accounts.entity.Account
import com.moronlu18.accounts.entity.AccountState
import com.moronlu18.accounts.entity.Email
import com.moronlu18.accounts.network.Resource
//import com.moronlu18.accounts.network.Resource


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

const val TAG = "ViewModel"

class AuthFirebase {
    private var authFirebase = FirebaseAuth.getInstance()
    private lateinit var account: Account

    //ght123456@hotmail.es
    //Esto se queda suspendido, para algo hay un pause.
    //
    //user!!.hashCode()
    //Lo que queremos recoger un dato de una función suspendida.
    //user!!.uid
    suspend fun login(email: String, password: String): Resource {
        return withContext(Dispatchers.IO) {

            try {

                //Este pausa, indica que se pause que termina la operación.
                val authResult: AuthResult =
                    authFirebase.signInWithEmailAndPassword(email, password).await()
                val user = authResult.user
                account = Account.create(
                    user!!.hashCode(),
                    Email(email),
                    password,
                    authResult.user!!.displayName,
                    AccountState.VERIFIED
                )
                Log.i(TAG, "El password es correcto")
                Resource.Success(account)
            } catch (e: Exception) {
                Log.i(TAG, "El password es incorrecto")
                Resource.Error(e)
            }
        }

    }

    /*
    suspend fun login1(email: String, password: String): Resource {

        //Este  código se ejecuta en un hilo especifico para operaciones entrada/salida (IO)
        withContext(Dispatchers.IO) {
            delay(3000)
            //Se ejecutara el codigo de consulta a FireBase. Que puede tardar más de 5s y en ese
            //caso se obtiene el error ANR (Android Not Responding) porque puede bloquear la vista
            try {
                val authResult: AuthResult =
                    authFirebase.signInWithEmailAndPassword(email, password).await()
                val user = authResult.user
                account = Account.create(
                   user!!.uid,
                    Email(email),
                    password,
                    authResult.additionalUserInfo!!.username,
                    AccountState.UNVERIFIED
                )
                //val account :Account = Account.create(us!!.uid.toInt(),user.email, password, null, AccountState.UNVERIFIED)

                Log.i(TAG, "El password es success")
                Resource.Success(account)
            } catch (e: Exception) {
                Log.i(TAG, "El password es incorrecto")
                Resource.Error(e)
                //Resource.Error(Exception("El password es incorrecto"))
            }
        }
        Log.i(TAG, "El password es correcto")
        return Resource.Error(Exception("El password es incorrecto"))
    }*/
}



