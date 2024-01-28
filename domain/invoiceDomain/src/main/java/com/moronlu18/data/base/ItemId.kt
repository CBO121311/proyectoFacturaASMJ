package com.moronlu18.data.base

import com.moronlu18.accounts.entity.Item

data class ItemId (override val value: Int) : UniqueId(value),Comparable<ItemId>{
    override fun compareTo(other: ItemId): Int {
        return this.value.compareTo(other.value)
    }
}