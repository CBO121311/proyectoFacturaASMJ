package com.moronlu18.invoice

import android.app.Application

class InvoiceApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Locator.initWith(this)
    }
}