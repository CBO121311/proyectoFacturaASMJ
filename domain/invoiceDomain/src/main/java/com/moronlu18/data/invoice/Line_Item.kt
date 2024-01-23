package com.moronlu18.data.invoice

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Line_Item(
    val invoice_id: Int,
    val item_id:Int,
    var quantity:Int,
    var price:Double,
    val iva:Int
): Parcelable