package com.moronlu18.data.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.moronlu18.data.item.ItemType

@ProvidedTypeConverter
class ItemTypeConverter {
    @TypeConverter
    fun toTypeItem(value: Int): ItemType {
        return enumValues<ItemType>()[value]
    }

    @TypeConverter
    fun fromTypeItem(value: ItemType): Int {
        return value.ordinal
    }
}