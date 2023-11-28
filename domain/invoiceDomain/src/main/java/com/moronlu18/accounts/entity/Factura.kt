package com.moronlu18.accounts.entity

import android.os.Parcelable

import kotlinx.parcelize.Parcelize

@Parcelize
data class Factura(
    val id:Int,
    val customerId: Int,
    val number: Double,
    val status: InvoiceStatus,
    val issuedDate: String,
    val dueDate: String,
    val lineItems: List<Item>?) : Parcelable