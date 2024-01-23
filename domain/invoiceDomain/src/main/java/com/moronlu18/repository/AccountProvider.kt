package com.moronlu18.repository

import com.moronlu18.data.account.Account

class AccountProvider private constructor(){

    companion object {
        var AccountList: MutableList<Account> = mutableListOf()

        init {
            initDataSetAccount()
        }

        private fun initDataSetAccount() {

        }
    }
}