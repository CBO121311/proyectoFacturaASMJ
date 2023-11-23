package com.moronlu18.accounts.entity

data class Item(
    val id: Int,
    val image: Int,
    val name: String,
    val description: String,
    val type: String, // Enum: articulo o servicio
    val rate: Double,
    val taxable: Boolean,
)