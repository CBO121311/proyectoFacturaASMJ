package com.moronlu18.repository


import com.moronlu18.data.account.Email
import com.moronlu18.data.base.CustomerId
import com.moronlu18.data.base.InvoiceId
import com.moronlu18.data.base.ItemId
import com.moronlu18.data.invoice.LineItem
import com.moronlu18.data.customer.Customer
import com.moronlu18.data.invoice.Invoice
import com.moronlu18.data.invoice.InvoiceStatus
import com.moronlu18.network.ResourceList
import com.moronlu18.inovice.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.time.Duration
import java.time.Instant
import java.util.Calendar
import kotlin.random.Random

class InvoiceProvider private constructor() {
    companion object {
        var dataSet: MutableList<Invoice> = mutableListOf()

        init {
            initDataSetFactura()
        }

        private fun initDataSetFactura() {
            dataSet.add(
                Invoice(
                    id = InvoiceId(1),
                    customerId = CustomerId(1),
                    number = giveNumberInvoice(),
                    status = InvoiceStatus.PENDIENTE,
                    issuedDate = Instant.now(),
                    dueDate = Instant.now().plus(Duration.ofDays(30)),
                    lineItems = listOf(
                        LineItem(
                            InvoiceId(1),
                            ItemId(1),
                            1,
                            2.4,
                            21,
                            ),
                        LineItem(
                            InvoiceId(1),
                            ItemId(2),
                            1,
                            4.56,21
                        ),

                        )
                )
            )

            dataSet.add(
                Invoice(
                    id = InvoiceId(2),
                    CustomerId(2),
                    number = giveNumberInvoice(),
                    status = InvoiceStatus.PAGADA,
                    issuedDate = Instant.now(),
                    dueDate = Instant.now().plus(Duration.ofDays(15)),
                    lineItems = listOf(
                        LineItem(
                            InvoiceId(2),
                            ItemId(3),
                            1,
                            0.42,
                            1,

                            ),
                        LineItem(
                            InvoiceId(2),
                            ItemId(4),
                            1,
                            0.92,
                            1,
                        ),

                        )
                )
            )
            dataSet.add(
                Invoice(
                    id = InvoiceId(3),
                   customerId = CustomerId(4),
                    number = giveNumberInvoice(),
                    status = InvoiceStatus.PAGADA,
                    issuedDate = Instant.now(),
                    dueDate = Instant.now().plus(Duration.ofDays(15)),
                    lineItems = listOf(
                        LineItem(
                            InvoiceId(3),
                            ItemId(3),
                            2,
                            0.84,
                            1,

                            ),
                        LineItem(
                            InvoiceId(3),
                            ItemId(4),
                            2,
                            1.84,
                            1,
                        ),

                        )
                )
            )
            dataSet.add(
                Invoice(
                    id = InvoiceId(4),
                    customerId = CustomerId(4),
                    number = giveNumberInvoice(),
                    status = InvoiceStatus.VENCIDA,
                    issuedDate = Instant.now(),
                    dueDate = Instant.now().plus(Duration.ofDays(15)),
                    lineItems = listOf(
                        LineItem(
                            InvoiceId(4),
                            ItemId(1),
                            2,
                            5.04,
                            10,
                            ),
                        LineItem(
                            InvoiceId(4),
                            ItemId(5),
                            1,
                            3.8,
                            21,
                        ),

                        )
                )
            )

        }

        /**
         * Función que genera un string aleatorio para el número de la factura
         * que comienza por los cuatro digitos del año actual
         */
        fun generateNumberInvoice(): String {
            val anyoActual = Calendar.getInstance().get(Calendar.YEAR).toString()
            val longitudRestante = 8 - anyoActual.length
            val caracteresAleatorios = (1..longitudRestante)
                .map { Random.nextInt(0, 10).toString() }
                .joinToString("")

            return anyoActual + caracteresAleatorios
        }

        /**
         * Función que asigna un número a la factura comprobando que no
         * exista en ninguna otra
         */
        fun giveNumberInvoice(): String {
            lateinit var numberInvoice: String
            val numbers = dataSet.map { it.number }
            do {
                numberInvoice = generateNumberInvoice()
            } while (numbers.contains(numberInvoice))
            return numberInvoice


        }

        suspend fun getInvoiceList(): ResourceList {
            return withContext(Dispatchers.IO) {
                delay(2000)
                when {
                    dataSet.isEmpty() -> ResourceList.Error(Exception("Vacío"))
                    else -> ResourceList.Success(dataSet as ArrayList<Invoice>)
                }
            }
        }

        fun getListWithoutLoading(): ResourceList {
            return try {
                if (dataSet.isEmpty()) {
                    ResourceList.Error(Exception("Vacío"))
                } else {
                    ResourceList.Success(dataSet as ArrayList<Invoice>)
                }
            } catch (e: Exception) {
                ResourceList.Error(e)
            }
        }

        fun addOrUpdateInvoice(invoice: Invoice, pos: Int?) {
            if (pos == null) {
                dataSet.add(invoice)
            } else {
                dataSet[pos] = invoice
            }
        }

        fun deleteInvoice(pos: Int) {
            dataSet.removeAt(pos)
        }

        fun getPosByInvoice(invoice: Invoice): Int {
            return dataSet.indexOf(invoice)
        }

        fun getInvoicePos(position: Int): Invoice {
            return dataSet[position]
        }

        /**
         * Comprueba si el id de cliente está en Invoice
         */
        fun isCustomerReferenceFactura(idCli: Int): Boolean {
            return dataSet.any() { it.customerId.value == idCli }
        }

        fun obtainsId(): Int {
            return dataSet.maxByOrNull { it.id.value as Int }?.id?.value ?: 0
        }

        fun obtainsIdByInvoice(invoice: Invoice): Int {
            var id = 0
            for (item in dataSet) {
                if (item == invoice) {
                    id = item.id.value
                }
            }
            return id
        }

        fun itemReferenceInvoice(idItem: Int): Boolean {
            val listaItems = dataSet.map { it.lineItems }
            return listaItems.any { item ->
                item?.any { it.itemId.value == idItem } ?: false
            }
        }
    }
}
