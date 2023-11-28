package com.moronlu18.accounts.entity

import android.os.Parcelable
import com.moronlu18.accounts.enum.ItemType
import kotlinx.parcelize.Parcelize

@Parcelize
data class Item(
    val id: Int,
    val image: Int,
    val name: String,
    val description: String,
    val type: ItemType,
    val rate: Double,
    val taxable: Boolean,
) : Parcelable