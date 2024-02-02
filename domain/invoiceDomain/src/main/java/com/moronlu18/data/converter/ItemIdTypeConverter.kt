package com.moronlu18.data.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.moronlu18.data.base.ItemId
import com.moronlu18.data.item.VatItemType

@ProvidedTypeConverter
class ItemIdTypeConverter {
    @TypeConverter
    fun toItemId(value: Int): ItemId {
        return ItemId(value)
    }

    @TypeConverter
    fun fromItemId(itemId: ItemId): Int {
        return itemId.value
    }
}