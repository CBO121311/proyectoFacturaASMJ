package com.moronlu18.accounts.repository

import com.moronlu18.accounts.entity.Item
import com.moronlu18.accounts.entity.Line_Item
import com.moronlu18.accounts.enum_entity.ItemType
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
                    R.drawable.pizza,
                    "Pizza",
                    "Producto sección precocinados",
                    ItemType.ARTÍCULO,
                    2.52,
                    true
                )
            )

            dataSetItem.add(
                Item(
                    idItem++,
                    R.drawable.leche,
                    "Leche",
                    "Producto sección lacteos",
                    ItemType.ARTÍCULO,
                    1.20,
                    true
                )
            )

            dataSetItem.add(
                Item(
                    idItem++,
                    R.drawable.manzana,
                    "Manzana",
                    "Producto sección fruta",
                    ItemType.ARTÍCULO,
                    0.42,
                    true
                )
            )

            dataSetItem.add(
                Item(
                    idItem++,
                    R.drawable.panespelta,
                    "Pan de espelta",
                    "Producto sección panadería",
                    ItemType.ARTÍCULO,
                    0.92,
                    false
                )
            )

            dataSetItem.add(
                Item(
                    idItem++,
                    R.drawable.servicio,
                    "Repartidor",
                    "Repartir productos a clientes",
                    ItemType.SERVICIO,
                    3.8,
                    false
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
                suma += item.rate
            }
            return String.format("%.2f€", suma)
        }
    }
}