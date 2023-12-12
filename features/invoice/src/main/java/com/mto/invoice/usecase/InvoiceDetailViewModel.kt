package com.mto.invoice.usecase


import androidx.lifecycle.ViewModel
import com.moronlu18.accounts.entity.Item
import com.moronlu18.accounts.repository.CustomerProvider
import com.moronlu18.accounts.repository.ItemProvider

class InvoiceDetailViewModel: ViewModel() {
    fun giveNom(id:Int): String {
        return CustomerProvider.getNom(id)

    }

    fun giveTotal(lista: MutableList<Item>): String {
        return ItemProvider.getTotal(lista)
    }
}