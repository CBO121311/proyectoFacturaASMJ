package com.moronlu18.accounts.repository

import com.moronlu18.accounts.entity.Factura
import com.moronlu18.accounts.entity.InvoiceStatus
import com.moronlu18.accounts.entity.Item
import java.time.Instant
import java.time.LocalDate
import java.util.Date

class FacturaProvider private constructor() {
    companion object {
        var dataSet: MutableList<Factura> = mutableListOf()

 /*       suspend fun isCustomerReferenceFactura(nomCli: String?): Boolean {
            return dataSet.any() { it.cliente == nomCli }
        }*/
    }
}
