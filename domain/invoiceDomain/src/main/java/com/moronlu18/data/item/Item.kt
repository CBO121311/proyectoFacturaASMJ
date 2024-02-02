package com.moronlu18.accounts.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.ForeignKey.Companion.RESTRICT
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.moronlu18.data.base.ItemId
import com.moronlu18.data.converter.ItemIdTypeConverter
import com.moronlu18.data.converter.ItemTypeConverter
import com.moronlu18.data.converter.ItemVatTypeConverter
import com.moronlu18.data.item.ItemType
import com.moronlu18.data.item.VatItemType
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Entity(tableName = "item")
@Parcelize
data class Item(
    @TypeConverters(ItemIdTypeConverter::class)
    @PrimaryKey
    val id: @RawValue ItemId,
    @TypeConverters(ItemTypeConverter::class)
    val type: ItemType,
    @TypeConverters(ItemVatTypeConverter::class)
    val vat: VatItemType,
    val name: String,
    val price: Double,
    val description: String? = null,
    val photo: Int? = null,
    //val photo: Int,
) : Parcelable, Comparable<Item> {

    override fun compareTo(other: Item): Int {
        return name.lowercase().compareTo(other.name.lowercase())
    }

    companion object {
        fun create(
            id: Int,
            type: ItemType,
            vat: VatItemType,
            name: String,
            price: Double,
            description: String?,
            photo: Int?,
        ): Item {
            return Item(
                id = ItemId(id),
                type = type,
                vat = vat,
                name = name,
                price = price,
                description = description,
                photo = photo
            )
        }
    }
}