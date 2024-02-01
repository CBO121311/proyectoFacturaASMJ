package com.moronlu18.data.converter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream


@ProvidedTypeConverter
class PhotoTypeConverter {

    @TypeConverter
    fun fromBitmap(bitmap: Bitmap?): ByteArray? {
        return bitmap?.let {
            // Convertir el bitmap a un array de bytes
            val stream = ByteArrayOutputStream()
            it.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.toByteArray()
        }
    }

    @TypeConverter
    fun toBitmap(byteArray: ByteArray?): Bitmap? {
        return byteArray?.let {
            // Convertir el array de bytes a un bitmap
            BitmapFactory.decodeByteArray(it, 0, it.size)
        }
    }
}