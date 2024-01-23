package com.moronlu18.data.customer


import android.graphics.Bitmap
import android.os.Parcelable
import com.moronlu18.data.account.Email
import com.moronlu18.data.base.Entity
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue


@Parcelize
data class Customer(
    override val id: Int,
    val name: String,
    val email: @RawValue Email,
    val phone: String? = null,
    val city: String? = null,
    val address: String? = null,
    val photo: Bitmap? = null,
    val phototrial: Int? = null,

    ) : Parcelable, Comparable<Customer>, Entity<Int>(id) {
    override fun compareTo(other: Customer): Int {
        return name.lowercase().compareTo(other.name.lowercase())
    }
}


