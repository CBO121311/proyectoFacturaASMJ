package com.moronlu18.data.account

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.moronlu18.data.base.Email
import com.moronlu18.data.converter.AccountIdTypeConverter
import com.moronlu18.data.converter.EmailTypeConverter


//Patrón factory.
//Crea objeto de si mismo.

//Los constructores no pueden ser privado ya que nos devuelve el DAO


//Tiene
/*1ºEntidad que tiene relacion
2ºLa columna de la tabla businness
3º El atributo de la tabla account*/
@Entity(
    tableName = "account", foreignKeys = [
        ForeignKey(
            entity = BusinessProfile::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("businessProfile"),
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
class Account(

    //Crea un nuevo accountid con el account de la base de datos.
    //Y al revés también.

    //Hay que comprobar el id, el email y el state.
    @PrimaryKey
    @TypeConverters(AccountIdTypeConverter::class)
    val accountId: AccountId,
    @TypeConverters(EmailTypeConverter::class)
    val email: Email,
    val password: String,
    val displayName: String?,
    var state: AccountState = AccountState.UNVERIFIED, //Fk
    //private var businessProfile: BusinessProfile, //Fk -> 1-1 (1,0 - 1,1).
    var businessProfile: Int?
) {
    //Para que lo genere la base de datos.
    //@PrimaryKey(autoGenerate = true)
    //var id:Int =0

    //businessProfile es una ForeignKey,
    //En el selectOriginal no queremos meter el businessProfile.
    //Vamos a hacer una conversión de tipos.


    /**
     * Al utilizar un objeto acompañante con una función y el constructor privado de la clase
     * garantizo el modo/restricciones que  tengo al crear un objeto de la clase.
     */
    //Se hace para tener control de la propiedad sobre ellos.
    //Y así garantizo que todas las cuenta sean no verificados.
    companion object {
        fun create(
            id: AccountId,
            email: Email,
            password: String,
            displayName: String?,
            state: AccountState,
            businessProfile: Int?
        ): Account {
            return Account(
                accountId = id,
                email = email,
                password = password,
                displayName = displayName,
                state = AccountState.UNVERIFIED, //state=state,
                businessProfile = businessProfile,
            )
        }

    }

}