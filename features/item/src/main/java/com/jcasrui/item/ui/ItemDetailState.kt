package com.jcasrui.item.ui

sealed class ItemDetailState {
    data object ReferencedItem : ItemDetailState()
    data object OnSuccess : ItemDetailState()
}