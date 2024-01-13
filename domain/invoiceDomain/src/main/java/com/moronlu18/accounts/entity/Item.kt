package com.moronlu18.accounts.entity

import android.os.Parcelable
import com.moronlu18.accounts.enum_entity.ItemType
import kotlinx.parcelize.Parcelize

@Parcelize
data class Item(
    val id: Int,
    val image: Int,
    val name: String,
    val description: String = "Sin descripción",
    val type: ItemType,
    val rate: Double,
    val taxable: Boolean,
) : Parcelable