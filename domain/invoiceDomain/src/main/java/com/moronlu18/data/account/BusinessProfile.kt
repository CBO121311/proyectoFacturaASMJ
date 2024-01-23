package com.moronlu18.data.account

import androidx.room.Entity
import androidx.room.PrimaryKey

//nombre dirección y n

@Entity(tableName = "businessprofile")
data class BusinessProfile(
    @PrimaryKey val id:Int,
    val name: String = "",
    val address: String = "",
    val phoneNumber: String = ""
) {
//No puede ser null en
}
