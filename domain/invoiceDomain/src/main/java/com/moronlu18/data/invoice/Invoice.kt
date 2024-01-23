package com.moronlu18.data.invoice

import android.os.Parcelable
import com.moronlu18.data.customer.Customer
import com.moronlu18.data.base.Entity
import kotlinx.parcelize.Parcelize
import java.time.Instant


@Parcelize
data class Invoice(
    override val id: Int,
    val customer: Customer,
    val number: String,
    val status: InvoiceStatus,
    val issuedDate: Instant,
    val dueDate: Instant,
    val lineItems: List<Line_Item>) : Parcelable, Entity<Int>(id)