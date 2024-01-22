package com.moronlu18.accounts.entity

import android.os.Parcelable
import com.moronlu18.accounts.enum_entity.ItemType
import com.moronlu18.accounts.enum_entity.VatType
import kotlinx.parcelize.Parcelize

@Parcelize
data class Item(
    val id: Int,
    val type: ItemType,
    val vat: VatType,
    val name: String,
    val price: Double,
    val description: String = "",
    //val photo: Int? = null,
    val photo: Int,
) : Parcelable
/*, Comparable<Item>{
    override fun compareTo(other: Item): Int {
        return name.lowercase().compareTo(other.name.lowercase())
    }
}*/