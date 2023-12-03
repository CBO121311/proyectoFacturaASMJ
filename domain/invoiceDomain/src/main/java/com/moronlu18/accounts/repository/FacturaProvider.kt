package com.moronlu18.accounts.repository

import com.moronlu18.accounts.entity.Factura


class FacturaProvider private constructor() {
    companion object {
        var dataSet: MutableList<Factura> = mutableListOf()

        fun isCustomerReferenceFactura(idCli: Int): Boolean {
            return dataSet.any() { it.customerId == idCli }
        }
    }
}
