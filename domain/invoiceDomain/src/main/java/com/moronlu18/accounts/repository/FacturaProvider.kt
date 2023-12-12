package com.moronlu18.accounts.repository

import com.moronlu18.accounts.entity.Factura
import com.moronlu18.accounts.network.ResourceList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FacturaProvider private constructor() {
    companion object {
        var dataSet: MutableList<Factura> = mutableListOf()

        suspend fun getInvoiceList(): ResourceList {
            return withContext(Dispatchers.IO) {
                when {
                    dataSet.isEmpty() -> ResourceList.Error(Exception("Vacío"))

                    else -> ResourceList.Success(dataSet as ArrayList<Factura>)
                }
            }
        }
        /**
         * Comprueba si el id de cliente está en Invoice
         */
        fun isCustomerReferenceFactura(idCli: Int): Boolean {
            return dataSet.any() { it.customerId == idCli }
        }
       fun obtainsId(): Int {
            return dataSet.maxByOrNull { it.id } ?.id?: 0
        }
    }
}
