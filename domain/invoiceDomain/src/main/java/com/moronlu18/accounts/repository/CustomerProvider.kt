package com.moronlu18.accounts.repository

import com.moronlu18.accounts.entity.Customer
import com.moronlu18.accounts.entity.Email
import com.moronlu18.inovice.R

class CustomerProvider private constructor() {
    companion object {

        var CustomerdataSet: MutableList<Customer> = mutableListOf()
        private var idCliente : Int = 1
        init {
            initDataSetCustomer()
        }


        private fun initDataSetCustomer() {
            CustomerdataSet.add(
                Customer(
                    idCliente++,
                    "Mr.Kiwi",
                    Email("kiwi@boss.com"),
                    "+34 123456789",
                    address = "Calle Plkjhgfd, 123",
                    photo = R.drawable.kiwituxedo
                )
            )
            CustomerdataSet.add(
                Customer(
                    idCliente++,
                    "Juan Pérez",
                    Email("juan@example.com"),
                    "+54 9 3541 12-3456",
                    "Madrid",
                    "Calle Principal, 123",
                    photo = R.drawable.liontuxedo
                )
            )

            CustomerdataSet.add(
                Customer(
                    idCliente++,
                    "María López",
                    Email("maria@example.com"),
                    "+525590633791",
                    "Barcelona",
                    "Avenida Central, 456", R.drawable.elephantuxedo
                )
            )
            CustomerdataSet.add(
                Customer(
                    idCliente++,
                    "Luis García",
                    Email("luis@example.com"),
                    "+34 111223344",
                    "Valencia",
                    "Paseo de la Playa, 789", R.drawable.kangorutuxedo
                )
            )
            CustomerdataSet.add(
                Customer(
                    idCliente,
                    "Alejandro López",
                    Email("al@example.com"),
                    photo = R.drawable.cbotuxedo
                )
            )
        }

        /* fun getCustomerId(id: Int): Customer {
             return dataSet.find { it.id == id }!!
         }*/

        fun getCustomer(): List<Customer>{
            return CustomerdataSet
        }

        fun getCustomerNameById(customerId: Int): String? {
            val customer = CustomerdataSet.find { it.id == customerId }
            return customer?.name
        }

        fun getCustomerPhotoById(customerId: Int): Int {
            val customer = CustomerdataSet.find { it.id == customerId }
            return customer?.photo ?: R.drawable.cebolla
        }



         fun deleteCustomer(customer: Customer): Boolean {
            val isReferenced = FacturaProvider.isCustomerReferenceFactura(customer.id) ||
                    TaskProvider.isCustomerReferenceTask(customer.id)

            if (isReferenced) {
                return true
            }
            return false
        }
       fun contains(nombre:String?): Boolean {
           for (item in CustomerdataSet) {
               if(item.name == nombre) {
                   return true
               }
           }
           return false
       }
        fun getNom(id:Int):String {
            lateinit var nombre:String
            for(item in CustomerdataSet) {
                if (item.id == id) {
                    nombre = item.name
                }
            }
            return nombre
        }
        fun getId(nom:String):Int {
            var id = 0
            for(item in CustomerdataSet) {
                if (item.name == nom) {
                    id = item.id
                }
            }
            return id
        }

    }
}