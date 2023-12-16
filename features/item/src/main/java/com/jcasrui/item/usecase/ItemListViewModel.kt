package com.jcasrui.item.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jcasrui.item.ui.ItemListState
import com.moronlu18.accounts.entity.Item
import com.moronlu18.accounts.repository.ItemProvider

class ItemListViewModel : ViewModel() {

    private var state = MutableLiveData<ItemListState>()

    fun getItem(): List<Item> {
        return ItemProvider.dataSetItem
    }

    fun getPositionItem(positionItem: Int): Item {
        return ItemProvider.getPosition(positionItem)
    }

    fun deleteItem(position: Int) {
        ItemProvider.dataSetItem.removeAt(position)
    }

    fun deleteItemSafe(item: Item): Boolean {
        return if (ItemProvider.referencedItem(item.id)) {
            state.value = ItemListState.ReferencedItem
            false
        } else {
            true
        }
    }

    fun onSuccess(){
        state.value = ItemListState.OnSuccess
    }

    fun getState(): LiveData<ItemListState> {
        return state
    }
}