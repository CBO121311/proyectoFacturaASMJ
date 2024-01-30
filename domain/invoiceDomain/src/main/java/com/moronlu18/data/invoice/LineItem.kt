package com.moronlu18.data.invoice

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.ForeignKey
import com.moronlu18.data.base.InvoiceId
import com.moronlu18.data.base.ItemId
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
@Entity(
    tableName = "line_item", foreignKeys = [
        ForeignKey(
            entity = InvoiceId::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("invoiceid"),
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ItemId::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("itemid"),
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
@Parcelize
data class LineItem(
    val invoiceId: @RawValue InvoiceId,
    val itemId: @RawValue ItemId,
    @NonNull
    var quantity:Int,
    @NonNull
    var price:Double,
    @NonNull
    val iva:Int
): Parcelable