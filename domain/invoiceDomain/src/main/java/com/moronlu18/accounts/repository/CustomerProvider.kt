package com.moronlu18.accounts.repository

import com.moronlu18.accounts.entity.Customer
import com.moronlu18.accounts.entity.Email
import com.moronlu18.accounts.network.ResourceList
import com.moronlu18.inovice.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class CustomerProvider private constructor() {
    companion object {


        var CustomerdataSet: MutableList<Customer> = mutableListOf()
        private var idCliente: Int = 1

        init {
            initDataSetCustomer()
        }

        private fun initDataSetCustomer() {
            //val resourceId = R.drawable.kiwituxedo
            //val bitmap = BitmapFactory.decodeResource(context.resources, resourceId)


            CustomerdataSet.add(
                Customer(
                    idCliente++,
                    "Mr.Kiwi",
                    Email("mrkiwi@example.com"),
                    "+54 9 3541 12-3456",
                    "Madrid",
                    "Calle Principal, 123",
                    phototrial = R.drawable.kiwituxedo
                    //phototrial = R.drawable.kiwituxedo
                )
            )

            CustomerdataSet.add(
                Customer(
                    idCliente++,
                    "María López",
                    Email("maria@example.com"),
                    "+525590633791",
                    "Barcelona",
                    "Avenida Central, 456", //R.drawable.elephantuxedo
                    phototrial = R.drawable.elephantuxedo
                )
            )
            CustomerdataSet.add(
                Customer(
                    idCliente++,
                    "Luis García",
                    Email("luis@example.com"),
                    "+34 111223344",
                    "Valencia",
                    "Paseo de la Playa, 789",
                    phototrial = R.drawable.kangorutuxedo
                    //photo = bitmap
                )
            )
            CustomerdataSet.add(
                Customer(
                    idCliente,
                    "Alejandro López",
                    Email("al@example.com"),
                    phototrial = R.drawable.cbotuxedo
                )
            )
        }

        /**
         * Comprueba si el idCustomer es válido y si existe en el repositorio
         */
        fun isCustomerExistOrNumeric(idCli: String, currentClient: Customer): Boolean {

            if (idCli.toIntOrNull() != null && idCli.toInt() > 0) {

                return CustomerdataSet.any { it.id == idCli.toInt() && it.id != currentClient.id }
            }
            return false
        }

        /**
         * Comprueba si el Customer está referenciado en otros repositorios y
         * es seguro borrarlo
         */
        fun isCustomerSafeDelete(customerId: Int): Boolean {

            return FacturaProvider.isCustomerReferenceFactura(customerId) ||
                    TaskProvider.isCustomerReferenceTask(customerId)
        }

        /**
         * Comprueba si tiene datos la lista
         * con un tiempo de espera de dos segundos
         */
        suspend fun getCustomerList(): ResourceList {
            return withContext(Dispatchers.IO) {
                delay(2000)
                when {
                    CustomerdataSet.isEmpty() -> ResourceList.Error(Exception("No hay datos"))

                    else -> ResourceList.Success(CustomerdataSet as ArrayList<Customer>)
                }
            }
        }


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
         * Añade o actualiza el customer
         */

        fun addOrUpdateCustomer(customer: Customer, pos: Int?) {

            if (pos == null) {
                CustomerdataSet.add(customer)
            } else {
                CustomerdataSet[pos] = customer
            }
        }

        /**
         * Borra un customer dependiendo de su posición
         */
        fun deleteCustomer(pos: Int) {
            CustomerdataSet.removeAt(pos)
        }

        /**
         * Obtener un Customer desde la posición
         */
        fun getCustomerPos(position: Int): Customer {
            return CustomerdataSet[position]
        }

        /**
         * Obtener el Id más alto dentro de la lista. Si es null devuelve cero.
         */
        fun getMaxCustomerid(): Int {
            return CustomerdataSet.maxByOrNull { it.id }?.id ?: 0
        }


        /**
         * Devuelve la posición de la lista en base al customer.
         */
        fun getPosByCustomer(customer: Customer): Int {
            return CustomerdataSet.indexOf(customer)
        }


        /**
         * Devuelve el customer por el id
         */
        fun getCustomerbyID(id: Int): Customer? {
            return CustomerdataSet.find { it.id == id }
        }


        fun getListCustomer(): List<Customer> {
            return CustomerdataSet
        }

        fun getCustomerNameById(customerId: Int): String? {
            val customer = CustomerdataSet.find { it.id == customerId }
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

        fun containsId(id: Int): Boolean {
            for (item in CustomerdataSet) {
                if (item.id == id) {
                    return true
                }
            }
            return false
        }

        fun getNom(id: Int): String {
            var nombre = "Cliente"
            for (item in CustomerdataSet) {
                if (item.id == id) {
                    nombre = item.name
                }
            }
            return nombre
        }

    }
}
//Todo Eliminado dos funciones de relleno
/*
fun getCustomerPhotoById(customerId: Int): Int {
    val customer = CustomerdataSet.find { it.id == customerId }
    return customer?.photo ?: R.drawable.cebolla
}

/*
fun getPhoto(id: Int): Int {
    var photo = 0
    for (item in CustomerdataSet) {
        if (item.id == id) {
            photo = item.photo
        }
    }
    return photo
}
*/