package com.moronlu18.accounts.base.customer


import com.moronlu18.data.account.AccountException
import com.moronlu18.data.base.Email
import org.junit.Assert
import org.junit.Test

class EmailTest {

    @Test
    fun `Email con formato valido`() {
        val email = Email("abc@outlook.com")

        Assert.assertEquals("abc@outlook.com", email.value)
    }

    @Test
    fun `Email con formato invalido`() {
        Assert.assertThrows(AccountException.InvalidEmailFormat::class.java){
            Email("email-mal")
        }
    }

    @Test
    fun `El toString() devuelve el valor en minusculas`() {
        val email = Email("AcD@GmAIl.com").toString()
        val emailminus = "acd@gmail.com"

        Assert.assertEquals(email, emailminus)
    }
}