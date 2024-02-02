package com.moronlu18.data.converter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream


@ProvidedTypeConverter
class PhotoTypeConverter {

    /**
     * Convierte el bitmap a un array de bytes
     */
    @TypeConverter
    fun fromBitmap(bitmap: Bitmap?): ByteArray? {
        return bitmap?.let {

            val stream = ByteArrayOutputStream()
            it.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.toByteArray()
        }
    }

    /**
     * Convierte el array de bytes a un bitmap
     */
    @TypeConverter
    fun toBitmap(byteArray: ByteArray?): Bitmap? {
        return byteArray?.let {
            BitmapFactory.decodeByteArray(it, 0, it.size)
        }
    }
}