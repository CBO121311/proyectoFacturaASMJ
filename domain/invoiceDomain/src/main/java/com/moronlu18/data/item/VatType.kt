package com.moronlu18.data.item

enum class VatType(val iva: Int) {
    ZERO(0),
    FOUR(4), // SUPERREDUCIDO
    FIVE(5), // SUPERREDUCIDO
    TEN(10), // REDUCIDO
    TWENTYONE(21) // GENERAL
}