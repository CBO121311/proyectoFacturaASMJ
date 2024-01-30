package com.moronlu18.data.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.moronlu18.data.invoice.InvoiceStatus

@ProvidedTypeConverter
class InvoiceStatusTypeConverter {
    @TypeConverter
    fun toType(value:Int): InvoiceStatus {
        return enumValues<InvoiceStatus>()[value]
    }
    fun fromType(value: InvoiceStatus): Int {
        return value.ordinal
    }
}