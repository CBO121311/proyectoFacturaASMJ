package com.cbo.customerlist.data.repository

import com.cbo.customerlist.data.model.Clientes

class ClientesProvider {
    companion object {
        val clientesList = listOf(
            Clientes(
                1,
                "Juan Pérez",
                "juan@example.com",
                "+123456789",
                "Madrid",
                "Calle Principal, 123"
            ),
            Clientes(
                2,
                "María López",
                "maria@example.com",
                "+987654321",
                "Barcelona",
                "Avenida Central, 456"
            ),
            Clientes(
                3,
                "Luis García",
                "luis@example.com",
                "+111223344",
                "Valencia",
                "Paseo de la Playa, 789"
            ),           Clientes(
                4,
                "Ana Martínez",
                "ana@example.com",
                "+555666777",
                "Sevilla",
                "Calle Sevilla, 42"
            ),
            Clientes(
                5,
                "Alejandro López",
                "al@example.com",
                "+999000111",
                "Málaga",
                "Calle perdices, 567"
            ),
            Clientes(
                6,
                "Sergio Ram",
                "sr@example.com",
                "+123456789",
                "Asturias",
                "Calle Principal, 123"
            ),
            Clientes(
                7,
                "Mateo",
                "mateo@example.com",
                "+987654321",
                "Cádiz",
                "Avenida Central, 456"
            ),
            Clientes(
                8,
                "Lourdes",
                "moronlu@example.com",
                "+111223344",
                "Antequera",
                "Centro, 789"
            ),           Clientes(
                9,
                "Paco",
                "paco@example.com",
                "+555666777",
                "Sevilla",
                "Calle Sevilla, 42"
            ),
            Clientes(
                10,
                "Jessica",
                "jes@example.com",
                "+999000111",
                "Málaga",
                "Avenida Málaga, 567"
            )
            // Agrega más clientes aquí
        )
    }
}