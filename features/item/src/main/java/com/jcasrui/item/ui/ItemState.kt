package com.jcasrui.item.ui

/**
 * 1. NameIsMandatory: nombre del artículo/servicio obligatorio
 * 2. RateIsMandatory: precio del artículo/servicio obligatorio
 * 3. InvalidId: el id del artículo es inválido.?
 */
sealed class ItemState {
    data object ReferencedItem : ItemState()
    data object NameIsMandatory : ItemState()
    data object RateIsMandatory : ItemState()

    //data object InvalidId : ItemState()
    data object OnSuccess : ItemState()
}