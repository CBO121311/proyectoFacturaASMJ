package com.mto.invoice.usecase

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.moronlu18.accounts.entity.Item
import com.moronlu18.data.base.InvoiceId
import com.moronlu18.data.invoice.Invoice
import com.moronlu18.data.invoice.LineItem
import com.moronlu18.repository.CustomerProvider
import com.moronlu18.repository.InvoiceProviderDB
import com.moronlu18.repository.ItemProviderBD
import com.moronlu18.repository.LineItemProviderDB
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
    var allItems= ItemProviderBD.getItemList().asLiveData()

    /**
     * Función que valida todos los parámetros necesarios para crear una factura
     */
    fun validateAll() {
        when {
            user.value.isNullOrBlank() -> state.value = InvoiceCreationState.CustomerUnspecified
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

    /**
     * Función que devuelve el state actual
     */
    fun getState() : LiveData<InvoiceCreationState> {
        return state
    }

    /**
     * Función que comprueba si las fechas están en el rango correcto
     */
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

    /**
     * Función que añade una factura a la base de datos
     */
    fun addRepository(invoice: Invoice) {
        InvoiceProviderDB.insert(invoice)
    }
    /**
     * Función que obtiene el id de la nueva factura que se va a crear
     */
    fun giveId(): Int {
        return InvoiceProviderDB.getNewId()
    }
    /**
     * Función que obtiene una factura a través de un id
     */
    fun getInvoiceById(id: Int): Invoice {
        return InvoiceProviderDB.getInvoiceById(id)
    }
    /**
     * Función que obtiene una lista de items en base al id de la factura
     */
    fun getListItemByInvoiceId(idInvoice: InvoiceId): List<Item> {
        return InvoiceProviderDB.getListItem(idInvoice)
    }
    /**
     * Función que obtiene el total de una lista de items de una factura
     */
    fun giveTotal(lista: List<Item>): String {
        val suma = lista.sumOf { it.price }
        return String.format("%.2f€", suma)
    }

    /**
     * Función que actualiza una factura de la base de datos
     */
    fun editRepository(invoice: Invoice) {
        InvoiceProviderDB.update(invoice)
    }

    /**
     * Función que crea un objeto lineItem y lo guarda en la base de datos
     */
    fun insertLineItem(lineItem: LineItem) {
        LineItemProviderDB.insert(lineItem)
    }

    /**
     * Función que borra la lista de line_items antigua y guarda los datos de la lista del modo
     * edición en la base de datos
     */
    fun deleteAndAddLineItems(invoiceId: InvoiceId) {
        val lista = LineItemProviderDB.getListItemsById(invoiceId.value)
        for (lineitem in lista) {
           LineItemProviderDB.delete(lineitem)
        }
    }

    /**
     * Función que obtiene el nombre del cliente dado su id, usado para mostrar su nombre en la interfaz
     * de creacion, en el botón de la parte superior derecha
     */
    fun giveNom(): String {
        val name = InvoiceProviderDB.getCustomerNameById(user.value.toString().toInt())
        return if (name.isNullOrBlank()) {
            "Cliente"
        }else {
            name
        }
    }
    /**
     * Función que obtiene el numero de la factura para ser creada
     */
    fun giveNumber():String {
        return InvoiceProviderDB.giveNumberInvoice()
    }

    /**
     * Función que inicializa la true la variable del modo edición
     */
    fun setEditorMode(isEditorMode: Boolean) {
        isEditor.value = isEditorMode
    }
    /**
     * Función que devuelve el valor de la variable edición
     */
    fun getEditorMode(): Boolean {
        return isEditor.value ?: false
    }


}
