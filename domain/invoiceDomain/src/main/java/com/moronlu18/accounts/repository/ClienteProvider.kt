package com.moronlu18.accounts.repository

import com.moronlu18.accounts.entity.Clientes
import com.moronlu18.inovice.R

class ClientesProvider {
    companion object {
        val clientesList = listOf(
            Clientes(
                0,
                "Mr.Kiwi",
                "kiwi@boss.com",
                "+123456789",
                "Nueva Zelanda",
                "Calle Plkjhgfd, 123",
                R.drawable.kiwituxedo
            ),
            Clientes(
                1,
                "Juan Pérez",
                "juan@example.com",
                "+123456789",
                "Madrid",
                "Calle Principal, 123",
                R.drawable.liontuxedo

            ),
            Clientes(
                2,
                "María López",
                "maria@example.com",
                "+987654321",
                "Barcelona",
                "Avenida Central, 456", R.drawable.elephantuxedo
            ),
            Clientes(
                3,
                "Luis García",
                "luis@example.com",
                "+111223344",
                "Valencia",
                "Paseo de la Playa, 789", R.drawable.kangorutuxedo
            ), Clientes(
                4,
                "Ana Martínez",
                "ana@example.com",
                "+555666777",
                "Sevilla",
                "Calle Sevilla, 42", R.drawable.dolphintuxedo
            ),
            Clientes(
                5,
                "Alejandro López",
                "al@example.com",
                "+999000111",
                "Málaga",
                "Calle perdices, 567",
                R.drawable.cbotuxedo
            ),
            Clientes(
                6,
                "Sergio Ram",
                "sr@example.com",
                "+123456789",
                "Asturias",
                "Calle Principal, 123",
                R.drawable.sharktuxedo
            ),
            Clientes(
                7,
                "Mateo",
                "mateo@example.com",
                "+987654321",
                "Cádiz",
                "Avenida Central, 456", R.drawable.tigretuxedo

            ),
            Clientes(
                8,
                "Lourdes",
                "moronlu@example.com",
                "+111223344",
                "Antequera",
                "Centro, 789",
                R.drawable.lynxtuxedo
            ), Clientes(
                9,
                "Paco",
                "paco@example.com",
                "+555666777",
                "Sevilla",
                "Calle Sevilla, 42", R.drawable.hipopotuxedo
            ),
            Clientes(
                10,
                "Jessica",
                "jes@example.com",
                "+999000111",
                "Málaga",
                "Avenida Málaga, 567", R.drawable.cougartuxedo
            )
        )

        val clientesListVacia = emptyList<Clientes>()

        fun getClienteId(id: Int): Clientes {

            return clientesList.find { it.id == id }!!
        }
    }
}