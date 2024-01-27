package com.moronlu18.data.invoice

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.moronlu18.data.base.CustomerId
import com.moronlu18.data.base.InvoiceId
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.time.Instant


@Entity(tableName = "invoice")
@Parcelize
data class Invoice(
    @PrimaryKey
    // @TypeConverters(InvoiceIdTypeConverter::class)
    val id: @RawValue InvoiceId, //val invoice:InvoiceId
    //@TypeConverters(CustomerIdTypeConverter::class)
    val customerId: @RawValue CustomerId,
    val number: String,
    val status: InvoiceStatus,
    //@TypeConverters(DateTypeConverter::class)
    val issuedDate: Instant,
    //@TypeConverters(DateTypeConverter::class)
    val dueDate: Instant,
    @Ignore
    val lineItems: List<LineItem>
) : Parcelable {
    /*
       constructor(
           invoice: Invoice,
           customerId: Customer,
           number: String,
           status: InvoiceStatus = InvoiceStatus.PENDIENTE,
           issuedDate: Instant,
           dueDate: Instant
       ) : (invoice, customerId, number, status, issuedDate, dueDate)

      companion object {
           fun create(
               invoice: Invoice,
               number: String,
               status: InvoiceStatus,
               customerId: Customer,
               issuedDate: Instant,
               dueDate: Instant
           )
       }*/
}

