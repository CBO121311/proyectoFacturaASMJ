package com.moronlu18.invoice.utils

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import java.text.NumberFormat
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun Instant.format(): String {
    return DateTimeFormatter
        .ofPattern("dd/MM/yyyy")
        .withZone(ZoneOffset.UTC)
        .format(this.toJavaInstant())
}

fun Double.toCurrency(): String {
    return NumberFormat.getCurrencyInstance().format(this)
}