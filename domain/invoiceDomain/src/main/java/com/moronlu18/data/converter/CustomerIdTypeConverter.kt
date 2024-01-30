package com.moronlu18.data.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.moronlu18.data.base.CustomerId

@ProvidedTypeConverter
class CustomerIdTypeConverter {
    @TypeConverter
    fun toCustomerId(value: Int): CustomerId {
        return CustomerId(value)
    }

    @TypeConverter
    fun fromCustomerId(customerId: CustomerId): Int {
        return customerId.value
    }
}