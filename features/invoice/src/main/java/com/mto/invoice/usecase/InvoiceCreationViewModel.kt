package com.mto.invoice.usecase

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moronlu18.accounts.entity.Factura
import com.moronlu18.accounts.entity.Item
import com.moronlu18.accounts.repository.CustomerProvider
import com.moronlu18.accounts.repository.FacturaProvider
import com.moronlu18.accounts.repository.ItemProvider
import com.mto.invoice.adapter.AddItemCreationAdapter
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern


class InvoiceCreationViewModel: ViewModel() {

    var user = MutableLiveData<String>()
    var adapter = MutableLiveData<AddItemCreationAdapter>()
    var startDate = MutableLiveData<String>()
    var endDate = MutableLiveData<String>()
    private val pattern = Pattern.compile("([0-9]{2,})([/])([0-9]{2,})([/])([0-9]{4,})")
    private var state = MutableLiveData<InvoiceCreationState>()
    fun validateAll() {
        when {
            user.value == null-> state.value = InvoiceCreationState.CustomerUnspecified
            user.value!!.isEmpty() -> state.value = InvoiceCreationState.CustomerUnspecified
            !CustomerProvider.containsId(user.value!!.toString().toInt())-> state.value = InvoiceCreationState.CustomerNoExists
            TextUtils.isEmpty(startDate.value)-> state.value = InvoiceCreationState.StartDateEmptyError
            !pattern.matcher(startDate.value).matches() -> state.value = InvoiceCreationState.StartDateFormatError
            TextUtils.isEmpty(endDate.value)-> state.value = InvoiceCreationState.EndDateEmptyError
            !pattern.matcher(endDate.value).matches() -> state.value = InvoiceCreationState.EndDateFormatError
            incorrectRange(startDate.value.toString(), endDate.value.toString()) -> state.value = InvoiceCreationState.IncorrectDateRange
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
        val formato = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        try {
            val startD = LocalDate.parse(startDate,formato).atStartOfDay().toInstant(ZoneOffset.UTC)
            val endD = LocalDate.parse(endDate,formato).atStartOfDay().toInstant(ZoneOffset.UTC)
            return endD.isBefore(startD)
        } catch (e:Exception) {
            return false
        }
    }

    fun addRepository(factura: Factura) {
        FacturaProvider.dataSet.add(factura)
    }

    fun giveId(): Int {
        return FacturaProvider.obtainsId()
    }
    fun giveNom(): String {
        return CustomerProvider.getNom(user.value.toString().toInt())
    }

    fun giveTotal(lista: MutableList<Item>) : String{
        return ItemProvider.getTotal(lista)
    }
    fun giveListItem(): MutableList<Item> {
        return ItemProvider.dataSetItem
    }


}
