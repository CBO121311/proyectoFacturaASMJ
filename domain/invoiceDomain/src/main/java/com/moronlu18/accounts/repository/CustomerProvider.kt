package com.moronlu18.accounts.repository

import com.moronlu18.accounts.entity.Customer
import com.moronlu18.inovice.R

class CustomerProvider private constructor() {
    companion object {

        var CustomerdataSet: MutableList<Customer> = mutableListOf()

        init {
            initDataSetCustomer()
        }

        private fun initDataSetCustomer() {
            CustomerdataSet.add(
                Customer(
                    0,
                    "Mr.Kiwi",
                    "kiwi@boss.com",
                    "+34 123456789",
                    address = "Calle Plkjhgfd, 123",
                    photo = R.drawable.kiwituxedo
                )
            )
            CustomerdataSet.add(
                Customer(
                    1,
                    "Juan Pérez",
                    "juan@example.com",
                    "+54 9 3541 12-3456",
                    "Madrid",
                    "Calle Principal, 123",
                    photo = R.drawable.liontuxedo
                )
            )

            CustomerdataSet.add(
                Customer(
                    2,
                    "María López",
                    "maria@example.com",
                    "+525590633791",
                    "Barcelona",
                    "Avenida Central, 456", R.drawable.elephantuxedo
                )
            )
            CustomerdataSet.add(
                Customer(
                    3,
                    "Luis García",
                    "luis@example.com",
                    "+34 111223344",
                    "Valencia",
                    "Paseo de la Playa, 789", R.drawable.kangorutuxedo
                )
            )
            CustomerdataSet.add(
                Customer(
                    4,
                    "Alejandro López",
                    "al@example.com",
                    photo = R.drawable.cbotuxedo
                )
            )
        }

        /* fun getCustomerId(id: Int): Customer {
             return dataSet.find { it.id == id }!!
         }*/

        fun getCustomerNames(): List<String> {
            return CustomerdataSet.map { it.name }
        }

        fun getCustomerById(id: Int): Customer? {
            return CustomerdataSet.find { it.id == id }
        }


        /*suspend fun deleteCustomer(customer: Customer): Boolean {
            val isReferenced = FacturaProvider.isCustomerReferenceFactura(customer.name) ||
                    TaskProvider.isCustomerReferenceTask(customer.name)

            if (isReferenced) {
                return true
            }
            return false
        }*/
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
            var id:Int = 0
            for(item in CustomerdataSet) {
                if (item.name == nom) {
                    id = item.id
                }
            }
            return id
        }

    }
}