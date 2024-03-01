package com.moronlu18.accounts.base.item

import com.moronlu18.data.base.ItemId
import junit.framework.TestCase.assertEquals
import org.junit.Assert
import org.junit.Test
import java.lang.IllegalArgumentException

class ItemIdTest {
    @Test
    fun `ItemId A es igual que B`() {
        val itemIdA = ItemId(2)
        val itemIdB = ItemId(2)

        Assert.assertEquals(itemIdA, itemIdB)
    }

    @Test
    fun `ItemId A diferente a B`() {
        val itemIdA = ItemId(1)
        val itemIdB = ItemId(2)

        Assert.assertNotEquals(itemIdA, itemIdB)
    }

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