package com.jcasrui.item.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jcasrui.item.ui.ItemListState
import com.moronlu18.accounts.entity.Item
import com.moronlu18.invoice.Locator
import com.moronlu18.network.ResourceList
import com.moronlu18.repository.ItemProvider
import kotlinx.coroutines.launch

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

    fun getItemList() {
        viewModelScope.launch {
            state.value = ItemListState.Loading(true)
            val result = ItemProvider.getItemList()
            state.value = ItemListState.Loading(false)

            when (result) {
                is ResourceList.Success<*> -> {
                    val resultList = result.data as ArrayList<Item>

                    when(Locator.settingsPreferencesRepository.getSettingValue("itemsort","id")){
                        "id" -> resultList.sortBy { it.id }
                        "name_item" -> resultList.sortBy { it.name}
                        "price_asc" -> resultList.sortBy { it.price }
                        "price_desc" -> resultList.sortByDescending { it.price }
                    }
                    state.value = ItemListState.Success(resultList)
                }
                is ResourceList.Error -> state.value = ItemListState.NoData
            }
        }
    }

    fun onSuccess() {
        state.value = ItemListState.OnSuccess
    }

    fun getState(): LiveData<ItemListState> {
        return state
    }
}