package com.jcasrui.item.usecase

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jcasrui.item.ui.ItemState
import com.moronlu18.accounts.entity.Item
import com.moronlu18.accounts.repository.ItemProvider

//const val TAG = "ViewModel"
class ItemCreationViewModel : ViewModel() {
    var nameItem = MutableLiveData<String>()
    var rateItem = MutableLiveData<String>()
    var prevItem: Item? = null
    private var isEditor = MutableLiveData<Boolean>()
    private var state = MutableLiveData<ItemState>()

    fun validateItem() {
        //Log.i(TAG, "El nombre es ${nameItem.value} y el precio es ${rateItem.value}")
        when {
            TextUtils.isEmpty(nameItem.value) -> state.value = ItemState.NameIsMandatory
            TextUtils.isEmpty(rateItem.value) -> state.value = ItemState.RateIsMandatory
            else -> state.value = ItemState.OnSuccess
        }
    }

    fun getNextId(): Int {
        return ItemProvider.getMaxId() + 1
    }

    fun getPositionItem(positionItem: Int): Item {
        return ItemProvider.getPosition(positionItem)
    }

    fun addItem(item: Item): Boolean {
        return ItemProvider.dataSetItem.add(item)
    }

    fun updateItem(item: Item, positionItem: Int) {
        ItemProvider.addUpdateItem(item, positionItem)
    }

    fun setEditor(isEditorMode: Boolean) {
        isEditor.value = isEditorMode
    }

    fun getEditor(): Boolean {
        return isEditor.value ?: false
    }

    fun getState(): LiveData<ItemState> {
        return state
    }
}