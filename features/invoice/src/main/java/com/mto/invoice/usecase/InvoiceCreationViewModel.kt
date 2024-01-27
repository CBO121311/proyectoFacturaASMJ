package com.mto.invoice.usecase

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moronlu18.accounts.entity.Item
import com.moronlu18.data.customer.Customer
import com.moronlu18.data.invoice.Invoice
import com.moronlu18.data.invoice.LineItem
import com.moronlu18.repository.CustomerProvider
import com.moronlu18.repository.InvoiceProvider
import com.moronlu18.repository.ItemProvider
import com.mto.invoice.adapter.creation.ItemCreationAdapter
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern


class InvoiceCreationViewModel: ViewModel() {

    var user = MutableLiveData<String>()
    var adapter = MutableLiveData<ItemCreationAdapter>()
    var startDate = MutableLiveData<String>()
    var endDate = MutableLiveData<String>()
    private var isEditor = MutableLiveData<Boolean>()
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
            adapter.value!!.itemCount == 0 -> state.value = InvoiceCreationState.AtLeastOneItem
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

    fun addRepository(invoice: Invoice) {
        InvoiceProvider.dataSet.add(invoice)
    }
    fun editRepository(invoice: Invoice, posInvoice: Int) {
        InvoiceProvider.addOrUpdateInvoice(invoice, posInvoice)
    }

    fun getInvoicePos(position:Int): Invoice {
        return InvoiceProvider.getInvoicePos(position)
    }
    fun getCustomerById(id:Int): Customer? {
        return CustomerProvider.getCustomerbyID(id)

    }

    fun giveId(): Int {
        return InvoiceProvider.obtainsId()
    }
    fun giveNom(): String {
        return CustomerProvider.getNom(user.value.toString().toInt())
    }
    fun giveIdEditor(invoice: Invoice): Int {
        return InvoiceProvider.obtainsIdByInvoice(invoice)
    }
    fun giveItemById(id:Int): Item {
        return ItemProvider.getItemById(id)!!
    }
    fun giveTotal(lista: MutableList<Item>) : String{
        return ItemProvider.getTotalItems(lista)
    }
    fun giveTotalLineItem(lista: MutableList<LineItem>) : String{
        return ItemProvider.getTotal(lista)
    }
    fun giveNumber():String {
        return InvoiceProvider.giveNumberInvoice()
    }
    fun giveListItem(): MutableList<Item> {
        return ItemProvider.dataSetItem
    }
    fun setEditorMode(isEditorMode: Boolean) {
        isEditor.value = isEditorMode
    }
    fun getEditorMode(): Boolean {
        return isEditor.value ?: false
    }


}
