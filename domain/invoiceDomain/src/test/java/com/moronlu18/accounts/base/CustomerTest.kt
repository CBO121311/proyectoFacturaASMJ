package com.moronlu18.accounts.base


import com.moronlu18.data.base.Email
import com.moronlu18.data.customer.Customer
import org.junit.Assert
import org.junit.Test

class CustomerTest {


    @Test
    fun `verificar Customer`() {

        val id= 1
        val name= "Alejandro"
        val email = Email("abc@hotmail.es")
        val phone = "654322111"
        val city = "Malaga"
        val address = "Calle Perdices Nº35"
        val photo = null


        val customer = Customer.create(
            id = id,
            name = name,
            email = email,
            phone = phone,
            city = city,
            address = address,
            photo = photo
        )

        Assert.assertEquals(id, customer.id.value)
        Assert.assertEquals(name, customer.name)
        Assert.assertEquals(email, customer.email)
        Assert.assertEquals(phone, customer.phone)
        Assert.assertEquals(city, customer.city)
        Assert.assertEquals(address, customer.address)
        Assert.assertEquals(photo, customer.photo)


    }

    @Test
    fun `Comparar customer con email iguales`() {
        val customer1 = Customer.create(
            id = 1,
            name = "Alejandro",
            email = Email("abc@hotmail.es"),
            phone = null,
            city = null,
            address = null,
            photo = null
        )

        val customer2 = Customer.create(
            id = 2,
            name = "López",
            email = Email("abc@hotmail.es"),
            phone = null,
            city = null,
            address = null,
            photo = null
        )
        Assert.assertEquals(0, customer1.compareTo(customer2))

    }


    @Test
    fun `Comparar customer con email que no son iguales`() {
        val customer1 = Customer.create(
            id = 1,
            name = "Alejandro",
            email = Email("abc@hotmail.es"),
            phone = null,
            city = null,
            address = null,
            photo = null
        )

        val customer2 = Customer.create(
            id = 2,
            name = "López",
            email = Email("jhgdd@hotmail.es"),
            phone = null,
            city = null,
            address = null,
            photo = null
        )
        Assert.assertEquals(-9, customer1.compareTo(customer2))

    }


}