package com.moronlu18.repository

import android.database.sqlite.SQLiteException
import com.moronlu18.data.base.ItemId
import com.moronlu18.data.invoice.LineItem
import com.moronlu18.database.InvoiceDatabase
import com.moronlu18.network.Resource

class LineItemProviderDB {
    companion object {
        fun insert(lineItem: LineItem): Resource {
            try {
                InvoiceDatabase.getInstance()?.lineItemDao()?.insert(lineItem)
            } catch (e: SQLiteException) {
                return Resource.Error(e)
            }
            return Resource.Success(lineItem)
        }
        fun delete(lineItem: LineItem) {
            InvoiceDatabase.getInstance().lineItemDao().delete(lineItem)
        }
        fun getListItemsById(id:Int): List<LineItem> {
            return InvoiceDatabase.getInstance().lineItemDao().getListItemsById(id)
        }
    }

    fun itemExist(itemId: ItemId): Boolean{
        return InvoiceDatabase.getInstance().lineItemDao().itemExistsLineItem(itemId)
    }
}