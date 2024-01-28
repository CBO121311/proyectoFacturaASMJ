package com.moronlu18.data.base

data class CustomerId(override val value: Int) : UniqueId(value),Comparable<CustomerId>{
    override fun compareTo(other: CustomerId): Int {
        return this.value.compareTo(other.value)
    }
}