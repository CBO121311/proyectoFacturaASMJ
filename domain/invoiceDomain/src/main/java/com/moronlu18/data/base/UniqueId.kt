package com.moronlu18.data.base

abstract class UniqueId(open val value:Any) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UniqueId

        return value == other.value
    }

    //ClassCastException da error ClassCastException
    //google truth

    override fun hashCode(): Int {
        return value.hashCode()
    }

    override fun toString(): String {
        return "$value"
    }
}