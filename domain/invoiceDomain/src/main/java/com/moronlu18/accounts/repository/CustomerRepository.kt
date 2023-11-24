package com.moronlu18.accounts.repository

import com.moronlu18.accounts.entity.Customer
import com.moronlu18.inovice.R

class CustomerRepository {
    companion object {
        val customerList = listOf(
            Customer(
                0,
                "Mr.Kiwi",
                "kiwi@boss.com",
                "+123456789",
                "Nueva Zelanda",
                "Calle Plkjhgfd, 123",
                R.drawable.kiwituxedo
            ),
            Customer(
                1,
                "Juan Pérez",
                "juan@example.com",
                "+123456789",
                "Madrid",
                "Calle Principal, 123",
                R.drawable.liontuxedo

            ),
            Customer(
                2,
                "María López",
                "maria@example.com",
                "+987654321",
                "Barcelona",
                "Avenida Central, 456", R.drawable.elephantuxedo
            ),
            Customer(
                3,
                "Luis García",
                "luis@example.com",
                "+111223344",
                "Valencia",
                "Paseo de la Playa, 789", R.drawable.kangorutuxedo
            ), Customer(
                4,
                "Ana Martínez",
                "ana@example.com",
                "+555666777",
                "Sevilla",
                "Calle Sevilla, 42", R.drawable.dolphintuxedo
            ),
            Customer(
                5,
                "Alejandro López",
                "al@example.com",
                "+999000111",
                "Málaga",
                "Calle perdices, 567",
                R.drawable.cbotuxedo
            ),
            Customer(
                6,
                "Sergio Ram",
                "sr@example.com",
                "+123456789",
                "Asturias",
                "Calle Principal, 123",
                R.drawable.sharktuxedo
            ),
            Customer(
                7,
                "Mateo",
                "mateo@example.com",
                "+987654321",
                "Cádiz",
                "Avenida Central, 456", R.drawable.tigretuxedo

            ),
            Customer(
                8,
                "Lourdes",
                "moronlu@example.com",
                "+111223344",
                "Antequera",
                "Centro, 789",
                R.drawable.lynxtuxedo
            ), Customer(
                9,
                "Paco",
                "paco@example.com",
                "+555666777",
                "Sevilla",
                "Calle Sevilla, 42", R.drawable.hipopotuxedo
            ),
            Customer(
                10,
                "Jessica",
                "jes@example.com",
                "+999000111",
                "Málaga",
                "Avenida Málaga, 567", R.drawable.cougartuxedo
            )
        )

        val customerListEmpty = emptyList<Customer>()
        //No se puede añadir una tarea si no hay clientes.
        //El cliente debe pasar un array de clientes
        fun getCustomerId(id: Int): Customer {
            return customerList.find { it.id == id }!!
        }
    }
}