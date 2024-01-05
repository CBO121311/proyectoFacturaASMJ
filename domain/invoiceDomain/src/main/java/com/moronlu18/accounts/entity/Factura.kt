package com.moronlu18.accounts.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.Instant

@Parcelize
data class Factura(
    val id:Int,
    val customer: Customer,
    val number: Double,
    val status: InvoiceStatus,
    val issuedDate: Instant,
    val dueDate: Instant,
    val lineItems: List<Item>?) : Parcelable