package com.jcasrui.item.usecase

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jcasrui.item.ui.ItemCreationState
import com.moronlu18.accounts.entity.Item
import com.moronlu18.repository.ItemProviderBD

//const val TAG = "ViewModel"
class ItemCreationViewModel : ViewModel() {
    var nameItem = MutableLiveData<String>()
    var rateItem = MutableLiveData<String>()
    private var state = MutableLiveData<ItemCreationState>()

    fun validateItem() {
        when {
            TextUtils.isEmpty(nameItem.value) -> state.value = ItemCreationState.NameIsMandatory
            TextUtils.isEmpty(rateItem.value) -> state.value = ItemCreationState.RateIsMandatory
            else -> state.value = ItemCreationState.OnSuccess
        }
    }

    fun getNextId(): Int {
        return (ItemProviderBD.getMaxId() ?: 0) + 1
    }

    fun addItem(item: Item) {
        ItemProviderBD.insert(item)
    }

    fun updateItem(item: Item) {
        ItemProviderBD.update(item)
    }

    fun getState(): LiveData<ItemCreationState> {
        return state
    }
}