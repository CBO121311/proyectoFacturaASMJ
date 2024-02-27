package com.moronlu18.accounts.base

import com.moronlu18.data.base.UniqueId
import org.junit.Assert
import org.junit.Test

class UniqueIdTest {
    @Test
    fun `id1 e id2 son iguales`() {
        val id1 = object : UniqueId(1) {}
        val id2 = object : UniqueId(1) {}

        Assert.assertEquals(id1, id2)
    }

    @Test
    fun `id1 e id2 no son iguales`() {
        val id1 = object : UniqueId(1) {}
        val id2 = object : UniqueId(2) {}

        Assert.assertNotEquals(id1.value, id2.value)
    }

    @Test
    fun `En hashCode si id1 e id2 tienen el mismo el valor es el mismo`() {
        val id1 = object : UniqueId(1) {}
        val id2 = object : UniqueId(1) {}

        Assert.assertEquals(id1.hashCode(), id2.hashCode())
    }

    @Test
    fun `El toString() devuelve el valor UniqueId en cadena`() {

        val value = 12
        val id = object : UniqueId(12) {}
        Assert.assertEquals(value.toString(), id.toString())
    }
}