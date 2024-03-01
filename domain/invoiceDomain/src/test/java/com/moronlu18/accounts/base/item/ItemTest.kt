package com.moronlu18.accounts.base.item

import com.moronlu18.accounts.entity.Item
import com.moronlu18.data.base.ItemId
import com.moronlu18.data.item.ItemType
import com.moronlu18.data.item.VatItemType
import junit.framework.TestCase.assertEquals
import org.junit.Assert
import org.junit.Test

class ItemTest {
    @Test
    fun `verificar creacion de un item`() {
        val id = 1
        val type = ItemType.SERVICE
        val vat = VatItemType.TWENTYONE
        val name = "Zanahoria"
        val price = 3.22
        val description = "Producto seccion verdura"
        val photo = null

        val item = Item.create(id, type, vat, name, price, description, photo)

        Assert.assertEquals(id, item.id.value)
        assertEquals(type, item.type)
        assertEquals(vat, item.vat)
        assertEquals(name, item.name)
        assertEquals(price, item.price, 0.0)
        assertEquals(description, item.description)
        assertEquals(photo, item.photo)
    }

    @Test
    fun `comparar un item con otro`() {
        val item1 = Item(ItemId(1), ItemType.SERVICE, VatItemType.TWENTYONE, "Zanahoria", 6.22)
        val item2 = Item(ItemId(2), ItemType.SERVICE, VatItemType.FIVE, "Zanahoria", 3.34)

        Assert.assertEquals(0, item1.compareTo(item2))
    }
}