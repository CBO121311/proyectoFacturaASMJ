package com.moronlu18.accounts.base.invoice

import com.moronlu18.data.base.InvoiceId
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test

class InvoiceIdTest {
    @Test
    fun `test de la creacion de invoiceid`() {
        val value = 1
        val invoiceId = InvoiceId(value)
        assertEquals(value, invoiceId.value)
    }

    @Test
    fun `test de la funcion compareTo de invoiceid`() {
        val invoiceId1 = InvoiceId(1)
        val invoiceId2 = InvoiceId(2)

        assertEquals(0, invoiceId1.compareTo(invoiceId1)) // Should be equal
        assertTrue(invoiceId1 < invoiceId2) // Should be less than
    }
}