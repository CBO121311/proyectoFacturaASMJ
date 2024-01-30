package com.moronlu18.invoice

import android.app.Application
import com.google.firebase.FirebaseApp

class InvoiceApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        //Se inicializa las preferencias y DataStore
        Locator.initWith(this)

        //Se inicializa la  conexión a FireBase
        FirebaseApp.initializeApp(this)
    }
}