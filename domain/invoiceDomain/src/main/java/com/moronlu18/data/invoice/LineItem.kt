package com.moronlu18.data.invoice

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.TypeConverters
import com.moronlu18.accounts.entity.Item
import com.moronlu18.data.base.InvoiceId
import com.moronlu18.data.base.ItemId
import com.moronlu18.data.converter.InvoiceIdTypeConverter
import com.moronlu18.data.converter.ItemIdTypeConverter
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
@Entity(
    tableName = "line_item",
    primaryKeys =["invoiceId","itemId"],
    foreignKeys = [
        ForeignKey(
            entity = Invoice::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("invoiceId"),
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Item::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("itemId"),
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
@Parcelize
data class LineItem(
    @TypeConverters(InvoiceIdTypeConverter::class)
    val invoiceId: @RawValue InvoiceId,
    @TypeConverters(ItemIdTypeConverter::class)
    val itemId: @RawValue ItemId,
    @NonNull
    var quantity:Int,
    @NonNull
    var price:Double,
    @NonNull
    val iva:Int
): Parcelable