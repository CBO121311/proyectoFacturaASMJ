package com.moronlu18.invoice

import android.app.Application

class InvoiceApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        //Se inicializa las preferencias y DataStore
        Locator.initWith(this)

        //Se inicializa la  conexión a FireBase
        //Firebase.initializeApp(this)
    }
}