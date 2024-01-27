package com.moronlu18.data.invoice

import android.os.Parcelable
import com.moronlu18.data.base.InvoiceId
import com.moronlu18.data.base.ItemId
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class LineItem(
    val invoiceId: @RawValue InvoiceId,
    val itemId: @RawValue ItemId,
    var quantity:Int,
    var price:Double,
    val iva:Int
): Parcelable