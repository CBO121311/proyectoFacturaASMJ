package com.moronlu18.accounts.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.moronlu18.data.base.ItemId
import com.moronlu18.data.item.ItemType
import com.moronlu18.data.item.VatType
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue



@Entity(tableName = "item")
@Parcelize
data class Item(
    @PrimaryKey
    val id: @RawValue ItemId,
    val type: ItemType,
    val vat: VatType,
    val name: String,
    val price: Double,
    val description: String = "",
    //val photo: Int? = null,
    val photo: Int,
) : Parcelable, Comparable<Item> {
    override fun compareTo(other: Item): Int {
        return name.lowercase().compareTo(other.name.lowercase())
    }
}