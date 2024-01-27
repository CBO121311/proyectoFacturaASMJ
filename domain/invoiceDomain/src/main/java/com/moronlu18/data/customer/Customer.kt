package com.moronlu18.data.customer


import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.moronlu18.data.account.Email
import com.moronlu18.data.base.CustomerId
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue


@Entity(tableName = "customer")
@Parcelize
data class Customer(
    @PrimaryKey
    val id: @RawValue CustomerId,
    val name: String,
    val email: @RawValue Email,
    val phone: String? = null,
    val city: String? = null,
    val address: String? = null,
    val photo: Bitmap? = null,
    val phototrial: Int? = null,

    ) : Parcelable, Comparable<Customer> {
    override fun compareTo(other: Customer): Int {
        return name.lowercase().compareTo(other.name.lowercase())
    }
}


