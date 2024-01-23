package com.jcasrui.item.ui

import com.moronlu18.accounts.entity.Item


/*
 * ReferencedItem: artículo referenciado.
 */
sealed class ItemListState {
    data object ReferencedItem : ItemListState()
    data object NoData : ItemListState()
    data object OnSuccess : ItemListState()
    data class Loading(val value: Boolean) : ItemListState()
    data class Success(val dataset: ArrayList<Item>) : ItemListState()
}