package com.moronlu18.data.base

open class Entity<T>(open val id: Int){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is Entity<*>) return false

        return id == other.id
    }

    override fun hashCode(): Int {
        return id
    }
}

/*interface  Entity{
    val baseId: Int
    val tag: String
}*/

//open class Entity<T : Any>(val id: Int, val data: T)

//val value: String get() = baseId.toString() + nameTag