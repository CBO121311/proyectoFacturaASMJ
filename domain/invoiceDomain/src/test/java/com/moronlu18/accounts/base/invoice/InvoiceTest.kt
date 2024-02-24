package com.moronlu18.accounts.base.invoice

import com.moronlu18.data.base.CustomerId
import com.moronlu18.data.base.InvoiceId
import com.moronlu18.data.invoice.Invoice
import com.moronlu18.data.invoice.InvoiceStatus
import com.moronlu18.data.invoice.LineItem
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.Instant
import java.time.temporal.ChronoUnit

class InvoiceTest {
    @Test
    fun `test de la funcion create de invoice`() {
        val id = InvoiceId(1)
        val customerId = CustomerId(2)
        val number = "INV-001"
        val status = InvoiceStatus.PENDIENTE
        val issuedDate = Instant.now()
        val dueDate = Instant.now().plus(30, ChronoUnit.DAYS)

        val invoice = Invoice.create(id.value, customerId.value, number, status, issuedDate, dueDate)

        assertEquals(id, invoice.id)
        assertEquals(customerId, invoice.customerId)
        assertEquals(number, invoice.number)
        assertEquals(status, invoice.status)
        assertEquals(issuedDate, invoice.issuedDate)
        assertEquals(dueDate, invoice.dueDate)
        assertEquals(emptyList<LineItem>(), invoice.lineItems)
    }

    @Test
    fun `test del constructor de invoice`() {
        val id = InvoiceId(1)
        val customerId = CustomerId(2)
        val number = "INV-002"
        val issuedDate = Instant.now()
        val dueDate = Instant.now().plus(30, ChronoUnit.DAYS)

        val invoice = Invoice(id, customerId, number, issuedDate = issuedDate, dueDate = dueDate)

        assertEquals(id, invoice.id)
        assertEquals(customerId, invoice.customerId)
        assertEquals(number, invoice.number)
        assertEquals(InvoiceStatus.PENDIENTE, invoice.status)
        assertEquals(issuedDate, invoice.issuedDate)
        assertEquals(dueDate, invoice.dueDate)
        assertEquals(emptyList<LineItem>(), invoice.lineItems)
    }

}