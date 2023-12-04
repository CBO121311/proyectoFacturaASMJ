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
sealed class CustomerCreationState {
    data object NameIsMandatory : CustomerCreationState()
    data object EmailEmptyError : CustomerCreationState()

    //data object NonExistentContact: CustomerCreationState()
    data object InvalidId : CustomerCreationState()
    data object InvalidEmailFormat : CustomerCreationState()

    //data object ReferencedCustomer: CustomerCreationState()

    //Escribe solo el nombre
    data object OnSuccess : CustomerCreationState()

    //todos estos son clases
    //data class AuthencationError(var message: String) : CustomerState()
    //data class Success(var account: Account) : CustomerState()

}