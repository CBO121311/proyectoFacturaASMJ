package com.moronlu18.accounts.base.account

import com.google.common.truth.Truth
import com.moronlu18.data.account.AccountException
import com.moronlu18.data.account.AccountId
import com.moronlu18.data.base.CustomerId
import org.junit.Assert
import org.junit.Test

class AccountIdTest {

    @Test
    fun `A es igual a B`(){
        //Configuración
        val a = AccountId(2)
        val b = AccountId(2)
        //assert al objeto
        //Valor que le doy, valor esperado.
        Truth.assertThat(a).isEqualTo(b)
        //   Truth.assertThat(a).isInstance
    }
    @Test
    fun `A es diferente a B`(){
        //Configuración
        val a = AccountId(3)
        val b = AccountId(2)

        Truth.assertThat(a).isNotEqualTo(b)
    }

    @Test
    fun `AccountId es igual a una instancia del mismo tipo`() {
        val a = AccountId(2)

        Truth.assertThat(a).isInstanceOf(AccountId::class.java)
    }

    @Test
    fun `AccountId no es igual a una instancia de otro tipo`() {
        val a = AccountId(2)

        Truth.assertThat(a).isNotInstanceOf(CustomerId::class.java)
    }

    @Test
    fun `AccountId valor negativo`() {
        Assert.assertThrows(AccountException::class.java){
            AccountId(-2)
        }
    }
}