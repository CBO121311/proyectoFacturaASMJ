package com.moronlu18.accounts.base.item

import com.moronlu18.accounts.entity.Item
import com.moronlu18.data.base.ItemId
import com.moronlu18.data.item.ItemType
import com.moronlu18.data.item.VatItemType
import org.junit.Assert
import org.junit.Test
import junit.framework.TestCase.assertEquals

class ItemTest {

    @Test
    fun `Item create devuelve un Item con los mismos valores`() {
        val id = 1
        val type = ItemType.PRODUCT
        val vat = VatItemType.TEN
        val name = "Leche"
        val price = 3.23
        val description = "Semidesnatada 6 litros"
        val photo = 0

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
    fun `Item compareTo`() {
        val item1 = Item(ItemId(1), ItemType.PRODUCT, VatItemType.FOUR, "El QuiJOte", 40.0)
        val item2 = Item(ItemId(2), ItemType.PRODUCT, VatItemType.ZERO, "El Quijote", 35.23)

        Assert.assertEquals(0, item1.compareTo(item2))
    }
}