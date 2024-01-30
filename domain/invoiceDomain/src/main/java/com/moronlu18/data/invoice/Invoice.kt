package com.moronlu18.data.invoice

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.moronlu18.data.base.CustomerId
import com.moronlu18.data.base.InvoiceId
import com.moronlu18.data.converter.CustomerIdTypeConverter
import com.moronlu18.data.converter.InstantConverter
import com.moronlu18.data.converter.InvoiceIdTypeConverter
import com.moronlu18.data.converter.InvoiceStatusTypeConverter
import com.moronlu18.data.customer.Customer
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.time.Instant


@Entity(tableName = "invoice", foreignKeys = [
    ForeignKey(
        entity = Customer::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("customerId"),
        onDelete = ForeignKey.RESTRICT,
        onUpdate = ForeignKey.CASCADE
    ),
])
@Parcelize
data class Invoice(
    @PrimaryKey
    @TypeConverters(InvoiceIdTypeConverter::class)
    val id: @RawValue InvoiceId,
    @TypeConverters(CustomerIdTypeConverter::class)
    val customerId: @RawValue CustomerId,
    val number: String,
    @TypeConverters(InvoiceStatusTypeConverter::class)
    val status: InvoiceStatus,
    @TypeConverters(InstantConverter::class)
    val issuedDate: Instant,
    @TypeConverters(InstantConverter::class)
    val dueDate: Instant,
    @Ignore
    val lineItems: List<LineItem>
) : Parcelable {

    constructor(
        id: InvoiceId,
        customerId: CustomerId,
        number: String,
        status: InvoiceStatus = InvoiceStatus.PENDIENTE,
        issuedDate: Instant,
        dueDate: Instant
    ) : this(id, customerId, number, status, issuedDate, dueDate, ArrayList<LineItem>())

    companion object {
        fun create(
            id: Int,
            customerId: Int,
            number: String,
            status: InvoiceStatus,
            issuedDate: Instant,
            dueDate: Instant
        ): Invoice {
            return Invoice(
                id = InvoiceId(id),
                customerId = CustomerId(customerId),
                number = number,
                status = status,
                issuedDate = issuedDate,
                dueDate = dueDate,
                lineItems = ArrayList()
            )
        }
    }
}

