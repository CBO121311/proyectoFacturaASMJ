package com.moronlu18.accounts.repository

import com.moronlu18.accounts.entity.Factura
import com.moronlu18.accounts.entity.InvoiceStatus
import com.moronlu18.accounts.entity.Item
import com.moronlu18.accounts.enum.ItemType
import com.moronlu18.accounts.network.ResourceList
import com.moronlu18.inovice.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Duration
import java.time.Instant

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

        init {
            initDataSetFactura()
        }

        private fun initDataSetFactura() {
            dataSet.add(
                Factura(
                    id = 1,
                    customerId = 1,
                    number =  3.72,
                    status = InvoiceStatus.Pendiente,
                    issuedDate = Instant.now(),
                    dueDate = Instant.now().plus(Duration.ofDays(30)),
                    lineItems = listOf(
                        Item(
                            1,
                            R.drawable.pizza,
                            "Pizza",
                            "Producto sección precocinados",
                            ItemType.ARTÍCULO,
                            2.52,
                            true
                        ),
                        Item(
                            2,
                            R.drawable.leche,
                            "Leche",
                            "Producto sección lacteos",
                            ItemType.ARTÍCULO,
                            1.20,
                            true
                        )
                    )
                )
            )

            dataSet.add(
                Factura(
                    id = 2,
                    customerId = 2,
                    number = 3.46,
                    status = InvoiceStatus.Pagada,
                    issuedDate = Instant.now(),
                    dueDate = Instant.now().plus(Duration.ofDays(15)),
                    lineItems = listOf(
                        Item(
                            3,
                            R.drawable.manzana,
                            "Manzana",
                            "Producto sección fruta",
                            ItemType.ARTÍCULO,
                            0.42,
                            true
                        ),
                        Item(
                            2,
                            R.drawable.leche,
                            "Leche",
                            "Producto sección lacteos",
                            ItemType.ARTÍCULO,
                            1.20,
                            true
                        ), Item(
                            4,
                            R.drawable.panespelta,
                            "Pan de espelta",
                            "Producto sección panadería",
                            ItemType.ARTÍCULO,
                            0.92,
                            false
                        ),Item(
                            4,
                            R.drawable.panespelta,
                            "Pan de espelta",
                            "Producto sección panadería",
                            ItemType.ARTÍCULO,
                            0.92,
                            false
                        )
                    )
                )
            )
        }


        /**
         * Comprueba si el id de cliente está en Invoice
         */
        fun isCustomerReferenceFactura(idCli: Int): Boolean {
            return dataSet.any() { it.customerId == idCli }
        }

        fun obtainsId(): Int {
            return dataSet.maxByOrNull { it.id }?.id ?: 0
        }
    }
}
