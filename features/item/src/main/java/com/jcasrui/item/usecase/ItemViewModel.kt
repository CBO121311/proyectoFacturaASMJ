package com.jcasrui.item.usecase

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jcasrui.item.ui.ItemState

//const val TAG = "ViewModel"

class ItemViewModel : ViewModel() {
    var nameItem = MutableLiveData<String>()
    var rateItem = MutableLiveData<String>()
    private var state = MutableLiveData<ItemState>()

    fun validateItem() {
        //Log.i(TAG, "El nombre es ${nameItem.value} y el precio es ${rateItem.value}")
        when {
            TextUtils.isEmpty(nameItem.value) -> state.value = ItemState.NameIsMandatory
            TextUtils.isEmpty(rateItem.value) -> state.value = ItemState.RateIsMandatory
            else -> state.value = ItemState.OnSuccess
        }
    }

    fun getState(): LiveData<ItemState> {
        return state
    }
}