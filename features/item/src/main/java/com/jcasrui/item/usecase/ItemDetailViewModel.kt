package com.jcasrui.item.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jcasrui.item.ui.ItemState
import com.moronlu18.accounts.entity.Item
import com.moronlu18.accounts.repository.ItemProvider

class ItemDetailViewModel : ViewModel() {
    private var state = MutableLiveData<ItemState>()


    fun getState(): LiveData<ItemState> {
        return state
    }
}