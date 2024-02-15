package com.jcasrui.item.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.jcasrui.item.ui.ItemListState
import com.moronlu18.repository.ItemProviderBD
import kotlinx.coroutines.launch

class ItemListViewModel : ViewModel() {

    private var state = MutableLiveData<ItemListState>()
    private val itemRepositoryBD = ItemProviderBD
    var allItem = itemRepositoryBD.getItemList().asLiveData()

    fun getItemList() {
        viewModelScope.launch {
            when {
                allItem.value?.isEmpty() == true -> state.value = ItemListState.NoData

                else -> {
                    state.value = ItemListState.OnSuccess
                }
            }

        }
    }

    fun getState(): LiveData<ItemListState> {
        return state
    }
}