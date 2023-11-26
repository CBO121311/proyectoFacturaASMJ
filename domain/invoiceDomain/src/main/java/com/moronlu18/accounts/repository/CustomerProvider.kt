package com.moronlu18.accounts.repository

import com.moronlu18.accounts.entity.Customer
import com.moronlu18.inovice.R

class CustomerProvider private constructor() {
    companion object {

        var dataSet: MutableList<Customer> = mutableListOf()

        init {
            initDataSetCustomer()
        }

        private fun initDataSetCustomer() {
            dataSet.add(
                Customer(
                    0,
                    "Mr.Kiwi",
                    "kiwi@boss.com",
                    "+34 123456789",
                    address = "Calle Plkjhgfd, 123",
                    photo = R.drawable.kiwituxedo
                )
            )
            dataSet.add(
                Customer(
                    1,
                    "Juan Pérez",
                    "juan@example.com",
                    "+54 9 3541 12-3456",
                    "Madrid",
                    "Calle Principal, 123",
                    photo =  R.drawable.liontuxedo
                )
            )

            dataSet.add(
                Customer(
                    2,
                    "María López",
                    "maria@example.com",
                    "+525590633791",
                    "Barcelona",
                    "Avenida Central, 456", R.drawable.elephantuxedo
                )
            )
            dataSet.add(
                Customer(
                    3,
                    "Luis García",
                    "luis@example.com",
                    "+34 111223344",
                    "Valencia",
                    "Paseo de la Playa, 789", R.drawable.kangorutuxedo
                )
            )
            dataSet.add(
                Customer(
                    4,
                    "Ana Martínez",
                    "ana@example.com",
                    "+34 555666777",
                    "Sevilla",
                    "Calle Sevilla, 42", R.drawable.dolphintuxedo
                )
            )
            dataSet.add(
                Customer(
                    5,
                    "Alejandro López",
                    "al@example.com",
                    photo = R.drawable.cbotuxedo
                )
            )
            dataSet.add(
                Customer(
                    6,
                    "Sergio Ram",
                    "sr@example.com",
                    "+34 123456789",
                    "Asturias",
                    "Calle Principal, 123",
                    R.drawable.sharktuxedo
                )
            )
            dataSet.add(
                Customer(
                    7,
                    "Mateo",
                    "mateo@example.com",
                    "+34 987654321",
                    "Cádiz",
                    "Avenida Central, 456", R.drawable.tigretuxedo
                )
            )
            dataSet.add(
                Customer(
                    8,
                    "Jessica",
                    "jes@example.com",
                    "+34 999000111",
                    "Málaga",
                    "Avenida Málaga, 567", R.drawable.cougartuxedo
                )
            )
        }

       /* fun getCustomerId(id: Int): Customer {
            return dataSet.find { it.id == id }!!
        }*/

        fun getCustomerNames(): List<String> {
            return dataSet.map { it.name }
        }

        fun getCustomerById(id: Int): Customer? {
            return dataSet.find { it.id == id }
        }
    }
}