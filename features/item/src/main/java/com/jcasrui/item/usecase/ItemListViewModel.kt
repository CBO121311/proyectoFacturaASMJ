package com.jcasrui.item.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.jcasrui.item.ui.ItemDetailState
import com.jcasrui.item.ui.ItemListState
import com.moronlu18.accounts.entity.Item
import com.moronlu18.invoice.Locator
import com.moronlu18.repository.ItemProviderBD
import com.moronlu18.repository.LineItemProviderDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ItemListViewModel : ViewModel() {

    private var state = MutableLiveData<ItemListState>()
    private val itemRepositoryBD = ItemProviderBD
    var allItem = itemRepositoryBD.getItemList().asLiveData()
    private var lineItemProviderDB = LineItemProviderDB()
    private var itemProviderDB = ItemProviderBD

    fun deleteItem(item: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            itemProviderDB.delete(item)
        }
    }

    fun deleteItemSafe(item: Item): Boolean {
        return if (lineItemProviderDB.itemExist(item.id)) {
            state.value = ItemListState.ReferencedItem
            false
        } else {
            true
        }
    }

    fun getItemList() {
        viewModelScope.launch {
            when {
                allItem.value?.isEmpty() == true -> state.value = ItemListState.NoData
                else -> {
                    allItem = when (Locator.settingsPreferencesRepository.getSettingValue(
                        "itemsort",
                        "id"
                    )) {
                        "id" -> itemProviderDB.getItemListPriceDesc().asLiveData()
                        "name_item" -> itemProviderDB.getItemListName().asLiveData()
                        "price_asc" -> itemProviderDB.getItemListPrice().asLiveData()
                        "price_desc" -> itemProviderDB.getItemListPriceDesc().asLiveData()
                        else -> itemProviderDB.getItemList().asLiveData()
                    }
                    state.value = ItemListState.OnSuccess
                }
            }
        }
    }

    fun getState(): LiveData<ItemListState> {
        return state
    }
}