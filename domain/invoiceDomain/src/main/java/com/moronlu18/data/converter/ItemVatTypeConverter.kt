package com.moronlu18.data.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.moronlu18.data.item.VatItemType

@ProvidedTypeConverter
class ItemVatTypeConverter {
    @TypeConverter
    fun toVatTypeItem(value: Int): VatItemType {
        return enumValues<VatItemType>()[value]
    }

    @TypeConverter
    fun fromVatTypeItem(value: VatItemType): Int {
        return value.ordinal
    }
}