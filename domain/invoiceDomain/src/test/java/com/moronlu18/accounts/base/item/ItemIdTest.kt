package com.moronlu18.accounts.base.item

import com.moronlu18.data.base.ItemId
import junit.framework.TestCase.assertEquals
import org.junit.Test

class ItemIdTest {

    @Test
    fun `Creacion ItemId`() {
        val value = 1
        val itemId = ItemId(value)
        assertEquals(value, itemId.value)
    }

    @Test
    fun `Compara los valores de dos ItemId`() {
        val itemId1 = ItemId(1)
        val itemId2 = ItemId(2)

        assertEquals(-1, itemId1.compareTo(itemId2))
    }
}