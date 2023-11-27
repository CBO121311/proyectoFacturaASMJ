package com.moronlu18.accounts.repository

import com.google.firebase.auth.FirebaseAuth

class AuthFirebaseRepository {
    private var authFirebase = FirebaseAuth.getInstance()


    fun login(email:String,password:String){}
}