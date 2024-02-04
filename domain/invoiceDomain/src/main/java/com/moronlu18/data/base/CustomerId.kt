package com.moronlu18.data.base

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class CustomerId(override val value: Int) : UniqueId(value),Parcelable, Comparable<CustomerId>{
    override fun compareTo(other: CustomerId): Int {
        return this.value.compareTo(other.value)
    }
}