package com.jcasrui.item.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jcasrui.item.ui.ItemState
import com.moronlu18.accounts.entity.Item
import com.moronlu18.repository.ItemProvider

class ItemDetailViewModel : ViewModel() {
    private var state = MutableLiveData<ItemState>()

    fun getPositionItem(positionItem: Int): Item {
        return ItemProvider.getPosition(positionItem)
    }
    fun getPosition(item: Item): Int {
        return ItemProvider.getPositionItem(item)
    }

    fun deleteItem(position: Int) {
        ItemProvider.dataSetItem.removeAt(position)
    }

    fun deleteItemSafe(item: Item): Boolean {
        return if (ItemProvider.referencedItem(item.id.value)) {
            state.value = ItemState.ReferencedItem
            false
        } else {
            true
        }
    }

    fun getState(): LiveData<ItemState> {
        return state
    }
}