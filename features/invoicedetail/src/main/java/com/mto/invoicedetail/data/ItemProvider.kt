package com.mto.invoicedetail.data

class ItemProvider {
    companion object{
        val itemList = listOf(
            Item(
                1,
                "Pizza",
                "Producto sección precocinados",
                "artículo",
                1.02,
                true
            ),
            Item(
                2,
                "Leche",
                "Producto sección lacteos",
                "artículo",
                0.80,
                true
            ),
            Item(
                3,
                "Manzana",
                "Producto sección fruta",
                "artículo",
                0.42,
                true
            ),
            Item(
                4,
                "Pan de espelta",
                "Producto sección panadería",
                "artículo",
                0.32,
                false
            ),
            Item(
                5,
                "Zanahoria",
                "Producto sección verdura",
                "artículo",
                0.56,
                true
            ),Item(
                6,
                "Fresa",
                "Producto sección fruta",
                "artículo",
                0.42,
                true
            ),
            Item(
                7,
                "Pan de maíz",
                "Producto sección panadería",
                "artículo",
                0.25,
                false
            ),
            Item(
                8,
                "Brocoli",
                "Producto sección verdura",
                "artículo",
                0.24,
                true
            ),
            Item(
                9,
                "Cebolla",
                "Producto sección verdura",
                "artículo",
                0.34,
                true
            ),
            Item(
                10,
                "Berenjena",
                "Producto sección verdura",
                "artículo",
                0.26,
                true
            ),
            Item(
                11,
                "Platano",
                "Producto sección fruta",
                "artículo",
                0.52,
                true
            ),
            Item(
                12,
                "Repartidor",
                "Repartir productos a clientes",
                "servicio",
                3.8,
                true
            )
        )
    }
}