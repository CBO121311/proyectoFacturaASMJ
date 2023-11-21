package com.mto.invoice.data.model

data class Item(
    val id: Int,
    val name: String,
    val description: String,
    val type: String, // articulo o servicio
    val rate: Double,
    val taxable: Boolean
)
