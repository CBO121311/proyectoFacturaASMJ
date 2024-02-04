package com.moronlu18.data.customer


import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.moronlu18.data.account.Email
import com.moronlu18.data.base.CustomerId
import com.moronlu18.data.converter.CustomerIdTypeConverter
import com.moronlu18.data.converter.EmailTypeConverter
import com.moronlu18.data.converter.PhotoTypeConverter
import com.moronlu18.data.converter.UriTypeConverter
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue


@Entity(tableName = "customer")
@Parcelize
data class Customer(
    @TypeConverters(CustomerIdTypeConverter::class)
    @PrimaryKey
    val id: CustomerId,
    var name: String,
    @TypeConverters(EmailTypeConverter::class)
    var email: Email,
    var phone: String? = null,
    var city: String? = null,
    var address: String? = null,
    @TypeConverters(PhotoTypeConverter::class)
    var photo: Bitmap? = null
    ) : Parcelable, Comparable<Customer> {
    override fun compareTo(other: Customer): Int {
        return name.lowercase().compareTo(other.name.lowercase())
    }
    companion object {
        fun create(
            id: Int,
            name: String,
            email: Email,
            phone: String?,
            city: String?,
            address: String?,
            photo: Bitmap
        ): Customer {
            return Customer(
                id = CustomerId(id),
                name = name,
                email = email,
                phone = phone,
                city = city,
                address = address,
                photo = photo
            )
        }
    }
}


