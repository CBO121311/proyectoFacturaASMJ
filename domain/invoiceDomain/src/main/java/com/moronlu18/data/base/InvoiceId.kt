package com.moronlu18.data.base

data class InvoiceId (override val value: Int) : UniqueId(value), Comparable<InvoiceId>{
    override fun compareTo(other: InvoiceId): Int {
        return this.value.compareTo(other.value)
    }
}