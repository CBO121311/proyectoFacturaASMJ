package com.moronlu18.data.customer


import android.net.Uri
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.moronlu18.data.base.Email
import com.moronlu18.data.base.CustomerId
import com.moronlu18.data.converter.CustomerIdTypeConverter
import com.moronlu18.data.converter.EmailTypeConverter
import com.moronlu18.data.converter.UriTypeConverter
import kotlinx.parcelize.Parcelize


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
    @TypeConverters(UriTypeConverter::class)
    var photo: Uri? = null
    ) : Parcelable, Comparable<Customer> {
    override fun compareTo(other: Customer): Int {
        return email.value.lowercase().compareTo(other.email.value.lowercase())
    }
    companion object {
        fun create(
            id: Int,
            name: String,
            email: Email,
            phone: String?,
            city: String?,
            address: String?,
            photo: Uri?
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


