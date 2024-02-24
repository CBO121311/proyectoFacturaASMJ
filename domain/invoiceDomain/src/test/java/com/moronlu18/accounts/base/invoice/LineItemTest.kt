package com.moronlu18.accounts.base.invoice

import com.moronlu18.data.base.InvoiceId
import com.moronlu18.data.base.ItemId
import com.moronlu18.data.invoice.LineItem
import junit.framework.TestCase.assertEquals
import org.junit.Test

class LineItemTest {
    @Test
    fun `test del constructor de line_item`() {
        val invoiceId = InvoiceId(1)
        val itemId = ItemId(2)
        val quantity = 5
        val price = 29.99
        val iva = 10

        val lineItem = LineItem(invoiceId, itemId, quantity, price, iva)

        assertEquals(invoiceId, lineItem.invoiceId)
        assertEquals(itemId, lineItem.itemId)
        assertEquals(quantity, lineItem.quantity)
        assertEquals(price, lineItem.price, 0.001)
        assertEquals(iva, lineItem.iva)
    }

}