package com.moronlu18.repository

import com.moronlu18.data.customer.Customer
import com.moronlu18.data.account.Email
import com.moronlu18.data.base.CustomerId
import com.moronlu18.network.ResourceList


class CustomerProvider private constructor() {
    companion object {

        var CustomerdataSet: MutableList<Customer> = mutableListOf()
        private var idCustomer: Int = 1

        init {
            initDataSetCustomer()
        }

        private fun initDataSetCustomer() {

            CustomerdataSet.add(
                Customer(
                    CustomerId(idCustomer++),
                    "Mr.Kiwi",
                    Email("kiwi@example.com"),
                    "+64 21 123 456",
                    "Auckland",
                    "Main Street, 123",
                    //phototrial = R.drawable.kiwituxedo
                )
            )

            CustomerdataSet.add(
                Customer(
                    CustomerId(idCustomer++),
                    "Maria Schmidt",
                    Email("schmidt@example.com"),
                    "+49 123456789",
                    "Berlín",
                    "Kurfürstendamm, 123", //R.drawable.elephantuxedo
                    //phototrial = R.drawable.elephantuxedo
                )
            )

            CustomerdataSet.add(
                Customer(
                    CustomerId(idCustomer++),
                    "Alejandro López",
                    Email("cebolla@example.com"),
                    //phototrial = R.drawable.cbotuxedo
                )
            )

            CustomerdataSet.add(
                Customer(
                    CustomerId(idCustomer),
                    "Zariel García",
                    Email("garc@example.com"),
                    "+34 687223344",
                    "Valencia",
                    "Avenida Reino de Valencia, 789",
                    //phototrial = R.drawable.kangorutuxedo
                )
            )
        }

        /**
         * Obtiene la lista de customers sin tiempo de espera.
         * Devuelve un objeto ResourceList.
         */
        fun getCustomerListNoLoading(): ResourceList {
            return try {
                if (CustomerdataSet.isEmpty()) {
                    ResourceList.Error(Exception("No hay datos"))
                } else {
                    ResourceList.Success(CustomerdataSet as ArrayList<Customer>)
                }
            } catch (e: Exception) {
                ResourceList.Error(e)
            }
        }

        /**
         * Obtiene un customer del repositorio según su posición.
         */
        fun getCustomerPos(position: Int): Customer {
            return CustomerdataSet[position]
        }

        /**
         * Obtiene el Id más alto dentro de la lista. Si es null, devuelve cero.
         */
        fun getMaxCustomerid(): Int {
            return CustomerdataSet.maxByOrNull { it.id.value as Int }?.id?.value  ?: 0
        }


        /**
         * Obtiene la posición de la lista en base a un customer.
         */
        fun getPosByCustomer(customer: Customer): Int {
            return CustomerdataSet.indexOf(customer)
        }


        /**
         * Obtiene un customer del repositorio según su id.
         */
        fun getCustomerbyID(id: Int): Customer? {
            return CustomerdataSet.find { it.id.value == id }
        }


        fun getListCustomer(): List<Customer> {
            return CustomerdataSet
        }

        fun getCustomerNameById(customerId: Int): String? {
            val customer = CustomerdataSet.find { it.id.value == customerId }
            return customer?.name
        }


        fun contains(nombre: String?): Boolean {
            for (item in CustomerdataSet) {
                if (item.name == nombre) {
                    return true
                }
            }
            return false
        }

    }
}