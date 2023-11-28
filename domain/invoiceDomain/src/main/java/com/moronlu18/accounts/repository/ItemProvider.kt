package com.moronlu18.accounts.repository

import com.moronlu18.accounts.entity.Item
import com.moronlu18.accounts.enum.ItemType
import com.moronlu18.inovice.R


class ItemProvider {
    companion object {

        var dataSetItem: MutableList<Item> = mutableListOf()

        init {
            initDataSetItem()
        }

        private fun initDataSetItem() {
            dataSetItem.add(
                Item(
                    1,
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
                    2,
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
                    3,
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
                    4,
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
                    5,
                    R.drawable.zanahoria,
                    "Zanahoria",
                    "Producto sección verdura",
                    ItemType.ARTÍCULO,
                    0.56,
                    true
                )
            )

            dataSetItem.add(
                Item(
                    6,
                    R.drawable.fresa,
                    "Fresa",
                    "Producto sección fruta",
                    ItemType.ARTÍCULO,
                    0.42,
                    true
                )
            )

            dataSetItem.add(
                Item(
                    7,
                    R.drawable.panmaiz,
                    "Pan de maíz",
                    "Producto sección panadería",
                    ItemType.ARTÍCULO,
                    0.85,
                    false
                )
            )

            dataSetItem.add(
                Item(
                    8,
                    R.drawable.brocoli,
                    "Brocoli",
                    "Producto sección verdura",
                    ItemType.ARTÍCULO,
                    0.34,
                    true
                )
            )

            dataSetItem.add(
                Item(
                    9,
                    R.drawable.cebolla,
                    "Cebolla",
                    "Producto sección verdura",
                    ItemType.ARTÍCULO,
                    0.39,
                    true
                )
            )

            dataSetItem.add(
                Item(
                    10,
                    R.drawable.berenjena,
                    "Berenjena",
                    "Producto sección verdura",
                    ItemType.ARTÍCULO,
                    0.26,
                    true
                )
            )

            dataSetItem.add(
                Item(
                    11,
                    R.drawable.platano,
                    "Platano",
                    "Producto sección fruta",
                    ItemType.ARTÍCULO,
                    0.52,
                    true
                )
            )

            dataSetItem.add(
                Item(
                    12,
                    R.drawable.servicio,
                    "Repartidor",
                    "Repartir productos a clientes",
                    ItemType.SERVICIO,
                    3.8,
                    false
                )
            )

        }
        fun getTotal(lista: MutableList<Item>) : String {
            var suma:Double = 0.0
            for (item in lista) {
                suma+= item.rate
            }
            return String.format("%.2f€",suma)
        }
    }
}