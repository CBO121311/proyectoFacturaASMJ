package com.jcasrui.item.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jcasrui.item.ui.ItemDetailState
import com.moronlu18.accounts.entity.Item
import com.moronlu18.repository.ItemProvider
import com.moronlu18.repository.ItemProviderBD
import com.moronlu18.repository.LineItemProviderDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ItemDetailViewModel : ViewModel() {
    private var state = MutableLiveData<ItemDetailState>()
    var id = MutableLiveData<String>()
    var type = MutableLiveData<String>()
    var vat = MutableLiveData<String>()
    var name = MutableLiveData<String>()
    var price = MutableLiveData<String>()
    var description = MutableLiveData<String>()
    private var lineItemProviderDB = LineItemProviderDB()
    private var itemProviderDB = ItemProviderBD

    fun deleteItem(item: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            itemProviderDB.delete(item)
        }
    }

    fun deleteItemSafe(item: Item): Boolean {
        return if (lineItemProviderDB.itemExist(item.id)) {
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