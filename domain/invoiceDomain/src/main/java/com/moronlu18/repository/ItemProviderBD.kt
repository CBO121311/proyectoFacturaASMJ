package com.moronlu18.repository

import android.database.sqlite.SQLiteException
import com.moronlu18.accounts.entity.Item
import com.moronlu18.database.InvoiceDatabase
import com.moronlu18.network.Resource
import kotlinx.coroutines.flow.Flow

class ItemProviderBD {
    companion object {
        fun insert(item: Item): Resource {
            try {
                InvoiceDatabase.getInstance().itemDao().insert(item)
            } catch (e: SQLiteException) {
                return Resource.Error(e)
            }
            return Resource.Success(item)
        }

        fun getItemList(): Flow<List<Item>> {
            return InvoiceDatabase.getInstance().itemDao().selectAll()
        }

        fun delete(item: Item) {
            InvoiceDatabase.getInstance().itemDao().delete(item)
        }
    }
}