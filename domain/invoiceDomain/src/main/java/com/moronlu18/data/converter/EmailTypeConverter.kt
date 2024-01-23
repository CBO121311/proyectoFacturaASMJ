package com.moronlu18.data.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.moronlu18.data.account.Email

@ProvidedTypeConverter
class EmailTypeConverter{
    @TypeConverter
    fun toEmail(value: String): Email = Email(value)

    @TypeConverter
    fun fromEmail(email: Email): String = email.value

}

