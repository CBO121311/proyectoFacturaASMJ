package com.moronlu18.data.base

import android.os.Parcelable
import com.moronlu18.data.account.AccountException
import kotlinx.parcelize.Parcelize
import java.util.regex.Pattern

@Parcelize
data class Email(val value: String):Parcelable {
    private val pattern = Pattern.compile("^\\S+@\\S+\\.\\S+")
    private val pattern2 = Pattern.compile("^[a-z-A-Z0-9._%+-]+@[a-zA-Z0-9]+\\.[a-zA-z]{3,}\$")
    //Init siempre se va a ejecutar si hay un objeto de esa clase
    init {
        if (!pattern.matcher(value).matches())   //matches coincidencia completa
        {
            throw AccountException.InvalidEmailFormat()
        }
    }

    override fun toString(): String {
        return value.lowercase()
    }
}
