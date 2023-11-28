package com.cbo.customer.ui

/*Esto es mi viewModel de SignIn que coge el genérico de Resource.
La clase sellada engloba todos los errores*/


/**
 * NameIsMandatory: nombre del cliente obligatorio
 * 2. NonExistentContact: el contacto no existe
 * 3. InvalidId: el id del cliente es inválido.
 * 4. InvalidEmailFormat: email con formato inválido
 * 5. ReferencedCustomer: cliente referenciado.
 */
sealed class CustomerState {
    data object NameIsMandatory : CustomerState()
    data object EmailEmptyError : CustomerState()

    data object NonExistentContact: CustomerState()
    data object InvalidId: CustomerState()
    data object InvalidEmailFormat: CustomerState()


    data object ReferencedCustomer: CustomerState()

   //Escribe solo el nombre
    data object OnSuccess : CustomerState()

    //todos estos son clases
    //data class AuthencationError(var message: String) : CustomerState()
    //data class Success(var account: Account) : CustomerState()

}