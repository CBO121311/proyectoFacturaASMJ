package com.moronlu18.accounts.repository

import com.moronlu18.accounts.entity.Customer
import com.moronlu18.accounts.entity.Factura
import com.moronlu18.accounts.entity.InvoiceStatus
import com.moronlu18.accounts.entity.Item
import com.moronlu18.inovice.R
import java.time.Instant
import java.time.LocalDate
import java.util.Date

class FacturaProvider private constructor() {
    companion object {
        var dataSet: MutableList<Factura> = mutableListOf()


        fun isCustomerReferenceFactura(idCli: Int): Boolean {
            return dataSet.any() { it.customerId == idCli }
        }
    }
}
