package com.jcasrui.item.ui

/**
 * 1. NameIsMandatory: nombre del cliente obligatorio
 * 2. InvalidId: el id del artículo es inválido.
 * 3. ReferencedItem: artículo referenciado.
 */
sealed class ItemState {
    data object NameIsMandatory : ItemState()
    data object RateIsMandatory : ItemState()
    data object InvalidId : ItemState()
    data object ReferencedItem : ItemState()
    data object OnSuccess : ItemState()
}