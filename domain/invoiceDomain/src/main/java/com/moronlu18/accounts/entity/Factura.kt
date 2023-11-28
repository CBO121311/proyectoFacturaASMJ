package com.moronlu18.accounts.entity

import android.os.Parcelable
import android.text.format.DateFormat
import kotlinx.parcelize.Parcelize
import java.time.Instant
import java.time.LocalDate
import java.util.Date

//data class Factura(val id:Int,val cliente:String, val fechaE:Instant, val fechaF:Instant, val total:Double, var items: MutableList<Item>?)

@Parcelize
data class Factura(
    val id:Int,
    val customerId: Int,
    val number: Int,
    val status: InvoiceStatus,
    val issuedDate: String,
    val dueDate: String,
    val lineItems: List<Item>?) : Parcelable