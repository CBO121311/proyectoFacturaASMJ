package com.moronlu18.accounts.entity


import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue


@Parcelize
data class Customer(
    val id: Int,
    val name: String,
    val email: @RawValue Email,
    val phone: String = "",
    val city: String = "",
    val address: String = "",
    val photo: Bitmap? = null,
    val phototrial: Int? = null
) : Parcelable, Comparable<Customer> {
    override fun compareTo(other: Customer): Int {
        return name.lowercase().compareTo(other.name.lowercase())
    }
}


