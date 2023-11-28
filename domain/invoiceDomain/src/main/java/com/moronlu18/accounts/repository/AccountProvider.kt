package com.moronlu18.accounts.repository

import com.moronlu18.accounts.entity.Account
import com.moronlu18.accounts.entity.Email

class AccountProvider private constructor(){

    companion object {

        var AccountList: MutableList<Account> = mutableListOf()

        init {
            AccountProvider.initDataSetAccount()
        }


        private fun initDataSetAccount() {

        }
    }
}