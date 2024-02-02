package com.jcasrui.item.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jcasrui.item.ui.ItemDetailState
import com.moronlu18.accounts.entity.Item
import com.moronlu18.repository.ItemProvider

class ItemDetailViewModel : ViewModel() {
    private var state = MutableLiveData<ItemDetailState>()
    var id = MutableLiveData<String>()
    var type = MutableLiveData<String>()
    var vat = MutableLiveData<String>()
    var name = MutableLiveData<String>()
    var price = MutableLiveData<String>()
    var description = MutableLiveData<String>()

    fun getPositionItem(positionItem: Int): Item {
        return ItemProvider.getPosition(positionItem)
    }

    fun getPosition(item: Item): Int {
        return ItemProvider.getPositionItem(item)
    }

    fun deletePosItem(position: Int) {
        ItemProvider.dataSetItem.removeAt(position)
    }
    fun deleteItem(item: Item){
        val position = getPosition(item)
        ItemProvider.dataSetItem.removeAt(position)
    }

    fun deleteItemSafe(item: Item): Boolean {
        return if (ItemProvider.referencedItem(item.id.value)) {
            state.value = ItemDetailState.ReferencedItem
            false
        } else {
            true
        }
    }

    fun getState(): LiveData<ItemDetailState> {
        return state
    }
}