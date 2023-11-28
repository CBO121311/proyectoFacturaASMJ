package com.moronlu18.accounts.entity

import java.util.regex.Pattern

data class Email(val value: String){
    private val pattern = Pattern.compile("")
    //Init siempre se va a ejecutar si hay un objeto de esa clase
    init {
        if (!pattern.matcher(value).matches())   //matches coincidencia completa
        {throw AccountException.InvalidEmailFormat() }

    }
}
