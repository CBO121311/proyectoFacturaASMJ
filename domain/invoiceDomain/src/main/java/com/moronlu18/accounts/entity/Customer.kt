package com.moronlu18.accounts.entity

data class Customer(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String="No disponible",
    val city: String="No disponible",
    val address:String="No disponible",
    val photo:Int
)