package com.mto.invoice.usecase

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moronlu18.accounts.repository.CustomerProvider
import com.mto.invoice.adapter.AddItemCreationAdapter
import java.util.regex.Pattern


const val TAG = "ViewModel"

class InvoiceCreationViewModel: ViewModel() {

    var user = MutableLiveData<String>()
    var adapter = MutableLiveData<AddItemCreationAdapter>()
    var startDate = MutableLiveData<String>()
    var endDate = MutableLiveData<String>()
    private val pattern = Pattern.compile("([0-9]{2,})([/])([0-9]{2,})([/])([0-9]{4,})")


    private var state = MutableLiveData<InvoiceCreationState>()


    fun validateAll() {
        when {
            TextUtils.isEmpty(user.value)-> state.value = InvoiceCreationState.CustomerUnspecified
            !CustomerProvider.contains(user.value)-> state.value = InvoiceCreationState.CustomerNoExists
            TextUtils.isEmpty(startDate.value)-> state.value = InvoiceCreationState.StartDateEmptyError
            !pattern.matcher(startDate.value).matches() -> state.value = InvoiceCreationState.StartDateFormatError
            TextUtils.isEmpty(endDate.value)-> state.value = InvoiceCreationState.EndDateEmptyError
            !pattern.matcher(endDate.value).matches() -> state.value = InvoiceCreationState.EndDateFormatError
            incorrectRange(startDate.value.toString(), endDate.value!!) -> state.value = InvoiceCreationState.IncorrectDateRange
            adapter.value == null -> state.value = InvoiceCreationState.AtLeastOneItem
            else -> {
                state.value = InvoiceCreationState.OnSuccess
            }
        }
    }

    fun getState() : LiveData<InvoiceCreationState> {
        return state
    }
    fun incorrectRange(startDate:String,endDate:String):Boolean {
        if(startDate=="null") {
            return true
        }else if(!pattern.matcher(startDate).matches() || !pattern.matcher(endDate).matches()) {
            return true
        } else{
            var num: Int = startDate.subSequence(7,startDate.length).toString().toInt()
            var num1: Int = endDate.subSequence(7,startDate.length).toString().toInt()
            if(num1 < num) {
                return true
            }else {
                num = startDate.subSequence(3,5).toString().toInt()
                num1 = endDate.subSequence(3,5).toString().toInt()
                if(num1 < num) {
                    return true;
                }else {
                    num = startDate.subSequence(0,2).toString().toInt()
                    num1 = endDate.subSequence(0,2).toString().toInt()
                    return num1 < num
                }
            }
        }
    }


}
