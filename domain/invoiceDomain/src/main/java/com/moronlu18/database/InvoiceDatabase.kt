package com.moronlu18.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.moronlu18.data.account.Account
import com.moronlu18.data.account.BusinessProfile
import com.moronlu18.data.account.Email
import com.moronlu18.data.account.User
import com.moronlu18.data.base.CustomerId
import com.moronlu18.data.converter.AccountIdTypeConverter
import com.moronlu18.data.converter.CustomerIdTypeConverter
import com.moronlu18.data.converter.EmailTypeConverter
import com.moronlu18.data.converter.PhotoTypeConverter
import com.moronlu18.data.converter.InstantConverter
import com.moronlu18.data.converter.InvoiceIdTypeConverter
import com.moronlu18.data.converter.InvoiceStatusTypeConverter
import com.moronlu18.data.customer.Customer
import com.moronlu18.data.converter.TaskIdTypeConverter
import com.moronlu18.data.converter.TaskStatusConverter
import com.moronlu18.data.converter.TaskTypeConverter
import com.moronlu18.data.invoice.Invoice
import com.moronlu18.data.task.Task
import com.moronlu18.database.dao.AccountDao
import com.moronlu18.database.dao.BusinessProfileDao
import com.moronlu18.database.dao.CustomerDao
import com.moronlu18.database.dao.InvoiceDao
import com.moronlu18.database.dao.TaskDao
import com.moronlu18.database.dao.UserDao
import com.moronlu18.inovice.R
import com.moronlu18.invoice.Locator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


@Database(
    entities = [Account::class, BusinessProfile::class, User::class, Task::class, Customer::class,
        Invoice::class],
    version = 2, //la version 2 hace que no pete ya que lo borra constantemente (???)
    exportSchema = false
)
//Hay que decir que convertidores vamos a utilizar
//El primero se lo paso por el parametro.
//El convertidores de tupo que vamos a utilizar
@TypeConverters(
    AccountIdTypeConverter::class,
    EmailTypeConverter::class,
    TaskIdTypeConverter::class,
    TaskStatusConverter::class,
    TaskTypeConverter::class,
    CustomerIdTypeConverter::class,
    PhotoTypeConverter::class,
    InstantConverter::class,
    InvoiceIdTypeConverter::class,
    InvoiceStatusTypeConverter::class,
)
abstract class InvoiceDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun accountDao(): AccountDao
    abstract fun invoiceDao(): InvoiceDao
    abstract fun taskDao(): TaskDao
    abstract fun businessProfileDao(): BusinessProfileDao
    abstract fun customerDao(): CustomerDao


    companion object {
        @Volatile
        private var INSTANCE: InvoiceDatabase? = null

        //Nosotros no propagamos desde el getInstance
        fun getInstance(): InvoiceDatabase {
            return INSTANCE ?: synchronized(InvoiceDatabase::class) {
                val instance = buildDatabase()
                INSTANCE = instance
                instance //Uno es nullable y otro no lo es
            }
        }

        //El contexto
        private fun buildDatabase(): InvoiceDatabase {
            return Room.databaseBuilder(
                Locator.requireApplication,
                InvoiceDatabase::class.java,
                "Invoice"
            ).fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                //y creamos un objeto de esta clase para convertir de un tipo a otro.
                .addTypeConverter(AccountIdTypeConverter())
                .addTypeConverter(EmailTypeConverter())
                .addTypeConverter(TaskStatusConverter())
                .addTypeConverter(TaskTypeConverter())
                .addTypeConverter(TaskIdTypeConverter())
                .addTypeConverter(CustomerIdTypeConverter())
                .addTypeConverter(PhotoTypeConverter())
                .addTypeConverter(InstantConverter())
                .addTypeConverter(InvoiceIdTypeConverter())
                .addTypeConverter(InvoiceStatusTypeConverter())
                .addCallback(
                    RoomDbInitializer(INSTANCE)
                    //Es una clase que implemente que la interfaz
                ).build()
        }
    }

    //Solo se le llama cuando se crea la base de datos
    class RoomDbInitializer(private val instance: InvoiceDatabase?) : Callback() {

        private val applicationScope = CoroutineScope(SupervisorJob())

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            runBlocking {

                applicationScope.launch(Dispatchers.IO) {
                    populateDatabase()

                }
            }


        }

        private suspend fun populateDatabase() {
            populateUsers()
            populateCustomer()
        }

        private suspend fun populateUsers() {

            val userDao = getInstance()?.userDao()
            userDao?.insert(User("Alejandro", "abc@hotmail.es"))
            userDao?.insert(User("Cristian", "rim@hotmail.es"))
            userDao?.insert(User("Sergio", "123cab@hotmail.es"))
            userDao?.insert(User("Jessica", "paella@hotmail.com"))
            userDao?.insert(User("Pedro", "op@hotmail.es"))
            userDao?.insert(User("Carlos", "mesa@gmail.com"))

            //Ejecuta este código si no es nulo
            /*instance.let { invoiceDatabase ->
                invoiceDatabase?.userDao()?.insert(
                    User("Alejandro", "abc@hotmail.es")
                )
                invoiceDatabase?.userDao()?.insert(
                    User("Cristian", "rim@hotmail.es")
                )
                invoiceDatabase?.userDao()?.insert(
                    User("Sergio", "123cab@hotmail.es")
                )
                invoiceDatabase?.userDao()?.insert(
                    User("Jessica", "paella@hotmail.com")
                )
                invoiceDatabase?.userDao()?.insert(
                    User("Pedro", "op@hotmail.es")
                )
                invoiceDatabase?.userDao()?.insert(
                    User("Carlos", "mesa@gmail.com")
                )
                Log.e("Circula", "Terminado")
            }*/
        }

        private fun populateCustomer() {
            var customId = 1;

            getInstance().customerDao().insert(
                Customer(
                    CustomerId(customId++), "Mr.Kiwi",
                    Email("kiwi@example.com"),
                    "+64 21 123 456",
                    "Auckland",
                    "Main Street, 123",
                    phototrial = R.drawable.kiwituxedo
                )
            )
            getInstance().customerDao().insert(
                Customer(
                    CustomerId(customId++), "Maria Schmidt",
                    Email("schmidt@example.com"),
                    "+49 123456789",
                    "Berlín",
                    "Kurfürstendamm, 123", //R.drawable.elephantuxedo
                    phototrial = R.drawable.elephantuxedo
                )
            )

            getInstance().customerDao().insert(
                Customer(
                    CustomerId(customId++), "Alejandro López",
                    Email("cebolla@example.com"),
                    phototrial = R.drawable.cbotuxedo
                )
            )

            getInstance().customerDao().insert(
                Customer(
                    CustomerId(customId),
                    "Zariel García",
                    Email("garc@example.com"),
                    "+34 687223344",
                    "Valencia",
                    "Avenida Reino de Valencia, 789",
                    phototrial = R.drawable.kangorutuxedo
                )
            )
        }
    }
}