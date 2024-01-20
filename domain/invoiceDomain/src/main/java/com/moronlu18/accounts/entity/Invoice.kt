package com.moronlu18.accounts.entity

import android.os.Parcelable
import com.moronlu18.accounts.enum_entity.InvoiceStatus
import kotlinx.parcelize.Parcelize
import java.time.Instant

@Parcelize
data class Invoice(
    val id:Int,
    val customer: Customer,
    val number: String,
    val status: InvoiceStatus,
    val issuedDate: Instant,
    val dueDate: Instant,
    val lineItems: List<Line_Item>?) : Parcelable