package com.moronlu18.data.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.moronlu18.data.base.InvoiceId

@ProvidedTypeConverter
class InvoiceIdTypeConverter {
    @TypeConverter
    fun toInvoiceId(value: Int): InvoiceId {
        return InvoiceId(value)
    }
    @TypeConverter
    fun fromInvoiceId(invoiceId: InvoiceId):Int{
        return invoiceId.value
    }

}