package com.moronlu18.accounts.entity

import android.os.Parcelable
import com.moronlu18.data.base.Entity
import com.moronlu18.data.item.ItemType
import com.moronlu18.data.item.VatType
import kotlinx.parcelize.Parcelize

@Parcelize
data class Item(
    override val id: Int,
    val type: ItemType,
    val vat: VatType,
    val name: String,
    val price: Double,
    val description: String = "",
    //val photo: Int? = null,
    val photo: Int,
) : Parcelable, Comparable<Item>, Entity<Int>(id) {
    override fun compareTo(other: Item): Int {
        return name.lowercase().compareTo(other.name.lowercase())
    }
}