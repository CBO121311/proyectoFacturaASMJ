package com.moronlu18.repository

import com.moronlu18.accounts.entity.Item
import com.moronlu18.data.base.ItemId
import com.moronlu18.data.invoice.LineItem
import com.moronlu18.data.item.ItemType

import com.moronlu18.data.item.VatType
import com.moronlu18.inovice.R
import com.moronlu18.network.ResourceList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class ItemProvider {
    companion object {

        var dataSetItem: MutableList<Item> = mutableListOf()
        private var idItem: Int = 1

        init {
            initDataSetItem()
        }

        private fun initDataSetItem() {
            dataSetItem.add(
                Item(
                    ItemId(idItem++),
                    ItemType.PRODUCT,
                    VatType.TWENTYONE,
                    "Pizza",
                    2.52,
                    "Producto sección precocinados",
                    R.drawable.pizza
                )
            )

            dataSetItem.add(
                Item(
                    ItemId(idItem++),
                    ItemType.PRODUCT,
                    VatType.TWENTYONE,
                    "Leche",
                    1.20,
                    "Producto sección lacteos",
                    R.drawable.leche,
                )
            )

            dataSetItem.add(
                Item(
                    ItemId(idItem++),
                    ItemType.PRODUCT,
                    VatType.TWENTYONE,
                    "Manzana",
                    0.42,
                    "Producto sección fruta",
                    R.drawable.manzana
                )
            )

            dataSetItem.add(
                Item(
                    ItemId(idItem++),
                    ItemType.PRODUCT,
                    VatType.FIVE,
                    "Pan de espelta",
                    0.92,
                    "Producto sección panadería",
                    R.drawable.panespelta
                )
            )

            dataSetItem.add(
                Item(
                    ItemId(idItem++),
                    ItemType.SERVICE,
                    VatType.TEN,
                    "Repartidor",
                    3.8,
                    "Repartir productos a clientes",
                    R.drawable.servicio,
                )
            )
        }

        fun getMaxId(): Int {
            return dataSetItem.maxByOrNull { it.id.value as Int }?.id?.value as Int? ?: 0
        }
        fun addUpdateItem(item: Item, positionItem: Int) {
            dataSetItem[positionItem] = item
        }

        fun getPosition(position: Int): Item {
            return dataSetItem[position]
        }

        fun getPositionItem(item: Item): Int {
            return dataSetItem.indexOf(item)
        }

        fun referencedItem(idItem: Int): Boolean {
            return InvoiceProvider.itemReferenceInvoice(idItem)
        }

        suspend fun getItemList(): ResourceList {
            return withContext(Dispatchers.IO) {
                delay(2000)
                when {
                    dataSetItem.isEmpty() -> ResourceList.Error(Exception("No data"))
                    else -> ResourceList.Success(dataSetItem as ArrayList<Item>)
                }
            }
        }

        /**
         * Función que devuelve un item dado un id
         */
        fun getItemById(id: Int): Item? {
            return dataSetItem.find { it.id.value == id }
        }

        fun getTotal(lista: MutableList<LineItem>): String {
            var suma: Double = 0.0
            for (item in lista) {
                suma += item.price
            }
            return String.format("%.2f€", suma)
        }

        fun getTotalItems(lista: MutableList<Item>): String {
            var suma: Double = 0.0
            for (item in lista) {
                suma += item.price
            }
            return String.format("%.2f€", suma)
        }
    }
}