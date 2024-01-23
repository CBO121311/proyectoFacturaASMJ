package com.moronlu18.data.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter

import com.moronlu18.data.account.AccountId

@ProvidedTypeConverter
class AccountIdTypeConverter {
    @TypeConverter
    fun toAccountId(value: Int): AccountId{
        return AccountId(value)
    }
    @TypeConverter
    fun fromAccountId(accountId: AccountId):Int{
        return accountId.value
    }

}