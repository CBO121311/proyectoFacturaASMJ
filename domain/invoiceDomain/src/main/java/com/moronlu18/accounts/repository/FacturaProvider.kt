package com.moronlu18.accounts.repository

import com.moronlu18.accounts.entity.Factura

class FacturaProvider {
    companion object {
        val facturaList = listOf(
            Factura(
                1,
                "Alejandro123",
                "10/10/2001",
                "12/12/2022",
                20.00
            ),
            Factura(
                2,
                "Jessica123",
                "20/1/2007",
                "12/10/2018",
                20.00
            ),
            Factura(
                3,
                "Sergio123",
                "1/12/2003",
                "1/2/2013",
                20.00
            ),
            Factura(
                4,
                "Mateo123",
                "12/12/2013",
                "12/12/2033",
                20.00
            ),
            Factura(
                5,
                "Alex123",
                "10/10/2001",
                "12/12/2022",
                20.00
            ),
            Factura(
                6,
                "Carlos123",
                "20/1/2007",
                "12/10/2018",
                20.00
            ),
            Factura(
                7,
                "Juan123",
                "1/12/2003",
                "1/2/2013",
                20.00
            ),
            Factura(
                8,
                "Lucas123",
                "12/12/2013",
                "12/12/2033",
                20.00
            )
            ,
            Factura(
                9,
                "Manolo123",
                "12/12/2013",
                "12/12/2033",
                20.00
            )
            ,
            Factura(
                10,
                "Fran123",
                "12/12/2013",
                "12/12/2033",
                20.00
            )
            ,
            Factura(
                11,
                "Pepe123",
                "12/12/2013",
                "12/12/2033",
                20.00
            )
            ,
            Factura(
                12,
                "Juan123",
                "12/12/2013",
                "12/12/2033",
                20.00
            )
            ,
            Factura(
                13,
                "Alberto123",
                "12/12/2013",
                "12/12/2033",
                20.00
            )

        )

        suspend fun isCustomerReferenceFactura(nomCli: String?): Boolean {
            return facturaList.any() { it.cliente == nomCli }
        }
    }
}

