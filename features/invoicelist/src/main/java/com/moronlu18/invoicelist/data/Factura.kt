package com.moronlu18.invoicelist.data

data class Factura(
    val id: Int,
    val customer: String,
    val issuedDate: String,
    val dueDate: String,
    val price: Double,
)