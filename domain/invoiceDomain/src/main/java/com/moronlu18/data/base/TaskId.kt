package com.moronlu18.data.base

data class TaskId(override val value: Int) : UniqueId(value), Comparable<TaskId> {
    override fun compareTo(other: TaskId): Int {
        return this.value.compareTo(other.value)
    }
}