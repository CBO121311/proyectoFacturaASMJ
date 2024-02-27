package com.moronlu18.accounts.base.customer

import com.google.common.truth.Truth
import com.moronlu18.data.base.CustomerId
import com.moronlu18.data.base.TaskId
import org.junit.Assert
import org.junit.Test
import java.lang.IllegalArgumentException

class CustomerIdTest {

    @Test
    fun `CustomerId A es el mismo que el B `(){

        val customerId1 = CustomerId(2)
        val customerId2 = CustomerId(2)

        Assert.assertEquals(customerId1,customerId2)
    }

    @Test
    fun `CustomerId A es distinto que B `(){

        val customerId1 = CustomerId(1)
        val customerId2 = CustomerId(2)

        Assert.assertNotEquals(customerId1,customerId2)
    }

    @Test
    fun `CustomerId es una instancia de CustomerId `(){

        val customerId1 = CustomerId(1)

        Assert.assertEquals(CustomerId::class.java,customerId1::class.java)
    }

    @Test
    fun `CustomerId no es una instancia de CustomerId `(){

        val customerId1 = CustomerId(1)
        Truth.assertThat(customerId1).isNotInstanceOf(TaskId::class.java)
    }

    @Test
    fun `CustomerId estan ordenados`(){
        val customerId1 = CustomerId(1)
        val customerId2 = CustomerId(2)

        Assert.assertTrue(customerId1 < customerId2)
    }

    @Test
    fun `CustomerId es menor que 0 `(){
        Assert.assertThrows(IllegalArgumentException::class.java){
            CustomerId(-1)
        }
    }
}