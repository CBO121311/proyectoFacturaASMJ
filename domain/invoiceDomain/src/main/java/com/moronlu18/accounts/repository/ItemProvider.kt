package com.moronlu18.accounts.repository

import com.moronlu18.accounts.entity.Item
import com.moronlu18.accounts.entity.Line_Item
import com.moronlu18.accounts.enum_entity.ItemType
import com.moronlu18.accounts.enum_entity.VatType
import com.moronlu18.inovice.R


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
                    idItem++,
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
                    idItem++,
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
                    idItem++,
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
                    idItem++,
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
                    idItem++,
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
            return dataSetItem.maxByOrNull { it.id }?.id ?: 0
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

        /**
         * Función que devuelve un item dado un id
         */
        fun getItemById(id:Int): Item? {
            return dataSetItem.find { it.id == id }
        }

        fun getTotal(lista: MutableList<Line_Item>): String {
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