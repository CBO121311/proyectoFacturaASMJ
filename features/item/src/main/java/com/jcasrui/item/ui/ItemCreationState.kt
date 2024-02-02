package com.jcasrui.item.ui

/**
 * 1. NameIsMandatory: nombre del artículo/servicio obligatorio
 * 2. RateIsMandatory: precio del artículo/servicio obligatorio
 * 3. InvalidId: el id del artículo es inválido.?
 */
sealed class ItemCreationState {
    //data object ReferencedItem : ItemCreationState()
    data object NameIsMandatory : ItemCreationState()
    data object RateIsMandatory : ItemCreationState()

    //data object InvalidId : ItemCreationState()
    data object OnSuccess : ItemCreationState()
}