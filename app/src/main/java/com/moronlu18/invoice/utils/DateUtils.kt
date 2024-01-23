package com.moronlu18.invoice.utils

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.datetime.*
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.LocalDate as JavaLocalDate
import java.time.LocalDateTime as JavaLocalDateTime




object DateUtils {
    fun now() = Clock.System.now()

    fun Instant.plusDays(days: Int) = this.plus(days, DateTimeUnit.DAY, TimeZone.UTC)

    @RequiresApi(Build.VERSION_CODES.O)
    fun parse(dateString: String): JavaLocalDateTime {
        var properString = dateString

        if (dateString.contains('-')) {
            properString = dateString.replace('-', '/')
        }

        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return JavaLocalDate.parse(properString, formatter).atTime(LocalTime.now())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun JavaLocalDateTime.toKotlinInstant(): Instant {
        return toInstant(ZoneOffset.UTC).toKotlinInstant()
    }

    fun nowX(): Instant {
        val timeZone = TimeZone.UTC
        val now = Clock.System.now().toLocalDateTime(timeZone)

        return with(now) {
            LocalDateTime(
                year, month, dayOfMonth, hour, minute, second
            ).toInstant(timeZone)
        }
    }
}