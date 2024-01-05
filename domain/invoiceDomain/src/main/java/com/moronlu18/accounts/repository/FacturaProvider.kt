package com.moronlu18.accounts.repository

import com.moronlu18.accounts.entity.Customer
import com.moronlu18.accounts.entity.Email
import com.moronlu18.accounts.entity.Factura
import com.moronlu18.accounts.entity.InvoiceStatus
import com.moronlu18.accounts.entity.Item
import com.moronlu18.accounts.enum.ItemType
import com.moronlu18.accounts.network.ResourceList
import com.moronlu18.inovice.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.time.Duration
import java.time.Instant

class FacturaProvider private constructor() {
    companion object {
        var dataSet: MutableList<Factura> = mutableListOf()

        init {
            initDataSetFactura()
        }

        private fun initDataSetFactura() {
            dataSet.add(
                Factura(
                    id = 1,
                    customer = Customer(
                        1,
                        "Mr.Kiwi",
                        Email("mrkiwi@example.com"),
                        "+64 21 123 456",
                        "Auckland",
                        "Main Street, 123",
                        phototrial = R.drawable.kiwituxedo
                    ),
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
                    customer = Customer(
                        2,
                        "Maria Schmidt",
                        Email("maria@example.com"),
                        "+49 123456789",
                        "Berlín",
                        "Kurfürstendamm, 123", //R.drawable.elephantuxedo
                        phototrial = R.drawable.elephantuxedo
                    ),
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


        suspend fun getInvoiceList(): ResourceList {
            return withContext(Dispatchers.IO) {
                delay(2000)
                when {
                    dataSet.isEmpty() -> ResourceList.Error(Exception("Vacío"))
                    else -> ResourceList.Success(dataSet as ArrayList<Factura>)
                }
            }
        }
        fun getListWithoutLoading(): ResourceList {
            return try {
                if (dataSet.isEmpty()) {
                    ResourceList.Error(Exception("Vacío"))
                } else {
                    ResourceList.Success(dataSet as ArrayList<Factura>)
                }
            } catch (e: Exception) {
                ResourceList.Error(e)
            }
        }

        fun addOrUpdateInvoice(factura: Factura, pos: Int?) {
            if (pos == null) {
                dataSet.add(factura)
            } else {
                dataSet[pos] = factura
            }
        }
        fun deleteInvoice(pos: Int) {
            dataSet.removeAt(pos)
        }

        fun getPosByInvoice(factura: Factura): Int {
            return dataSet.indexOf(factura)
        }

        fun getInvoicePos(position:Int): Factura {
            return dataSet[position]
        }

        /**
         * Comprueba si el id de cliente está en Invoice
         */
        fun isCustomerReferenceFactura(idCli: Int): Boolean {
            return dataSet.any() { it.customer.id == idCli }
        }

        fun obtainsId(): Int {
            return dataSet.maxByOrNull { it.id }?.id ?: 0
        }
        fun obtainsIdByInvoice(factura: Factura): Int {
            var id = 0
            for (item in dataSet) {
                if(item == factura) {
                    id = item.id
                }
            }
            return id
        }

        fun itemReferenceInvoice(idItem: Int): Boolean {
            return dataSet.any { invoice ->
                invoice.lineItems?.any { it.id == idItem } ?: false
            }
        }
    }
}
