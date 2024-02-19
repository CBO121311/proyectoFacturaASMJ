package com.moronlu18.accounts.base


import com.moronlu18.data.account.Account
import com.moronlu18.data.account.AccountException
import com.moronlu18.data.account.AccountId
import com.moronlu18.data.account.AccountState
import com.moronlu18.data.base.Email
import org.junit.Assert
import org.junit.Test

class AccountTest {
    //Configuración


    private val account = Account.create(
        id = AccountId(1),
        email = Email("cob@hotmail.es"),
        password = "123456",
        state = AccountState.VERIFIED,
        displayName = "CBO López",
        businessProfile = null
    )

    @Test
    fun `cambiar a estado verified`(){
        val accountState = AccountState.VERIFIED
        val stateString = accountState.toString()

        Assert.assertEquals("VERIFIED", stateString)
    }

    @Test
    fun `cambiar a estado unverified`(){
        val accountState = AccountState.UNVERIFIED
        val stateString = accountState.toString()

        Assert.assertEquals("UNVERIFIED", stateString)
    }

    @Test
    fun `email valido`(){
        val email = Email("cboo@hotmail.es")

        Assert.assertEquals("cboo@hotmail.es", email.value)
    }

    @Test
    fun `email invalido`(){
        Assert.assertThrows(AccountException.InvalidEmailFormat::class.java){
            Email("ffff")
        }
    }

    @Test
    fun `verificar accountId`() {
        val accountId = AccountId(1)
        Assert.assertEquals(accountId, account.accountId)
    }

    @Test
    fun `verificar email`() {
        val email = Email("cob@hotmail.es")
        Assert.assertEquals(email, account.email)
    }


    /*fun `email toString`() {
        val email = Email("cboo@hotmail.es")
        val emailString = email.toString()

        Assert.assertEquals("cboo@hotmail.es", emailString)
    }*/
}