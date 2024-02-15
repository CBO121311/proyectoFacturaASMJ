package com.moronlu18.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.moronlu18.accounts.entity.Item
import com.moronlu18.data.account.Account
import com.moronlu18.data.account.BusinessProfile
import com.moronlu18.data.base.Email
import com.moronlu18.data.account.User
import com.moronlu18.data.base.CustomerId
import com.moronlu18.data.base.InvoiceId
import com.moronlu18.data.base.ItemId
import com.moronlu18.data.base.TaskId
import com.moronlu18.data.converter.AccountIdTypeConverter
import com.moronlu18.data.converter.CustomerIdTypeConverter
import com.moronlu18.data.converter.EmailTypeConverter
import com.moronlu18.data.converter.InstantConverter
import com.moronlu18.data.converter.InvoiceIdTypeConverter
import com.moronlu18.data.converter.InvoiceStatusTypeConverter
import com.moronlu18.data.converter.ItemIdTypeConverter
import com.moronlu18.data.converter.ItemTypeConverter
import com.moronlu18.data.converter.ItemVatTypeConverter
import com.moronlu18.data.customer.Customer
import com.moronlu18.data.converter.TaskIdTypeConverter
import com.moronlu18.data.converter.TaskStatusConverter
import com.moronlu18.data.converter.TaskTypeConverter
import com.moronlu18.data.converter.UriTypeConverter
import com.moronlu18.data.invoice.Invoice
import com.moronlu18.data.invoice.InvoiceStatus
import com.moronlu18.data.invoice.LineItem
import com.moronlu18.data.item.ItemType
import com.moronlu18.data.item.VatItemType
import com.moronlu18.data.task.Task
import com.moronlu18.data.task.TaskStatus
import com.moronlu18.data.task.TypeTask
import com.moronlu18.database.dao.AccountDao
import com.moronlu18.database.dao.BusinessProfileDao
import com.moronlu18.database.dao.CustomerDao
import com.moronlu18.database.dao.InvoiceDao
import com.moronlu18.database.dao.ItemDao
import com.moronlu18.database.dao.lineItemDao
import com.moronlu18.database.dao.TaskDao
import com.moronlu18.database.dao.UserDao
import com.moronlu18.inovice.R
import com.moronlu18.invoice.Locator
import com.moronlu18.repository.InvoiceProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant


@Database(
    entities = [Account::class, BusinessProfile::class, User::class, Task::class, Customer::class, Invoice::class, Item::class, LineItem::class],
    version = 1,
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
    InstantConverter::class,
    InvoiceIdTypeConverter::class,
    InvoiceStatusTypeConverter::class,
    ItemIdTypeConverter::class,
    ItemTypeConverter::class,
    ItemVatTypeConverter::class,
    //PhotoTypeConverter::class,
    UriTypeConverter::class
)
abstract class InvoiceDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun accountDao(): AccountDao
    abstract fun invoiceDao(): InvoiceDao
    abstract fun lineItemDao(): lineItemDao
    abstract fun taskDao(): TaskDao
    abstract fun businessProfileDao(): BusinessProfileDao
    abstract fun customerDao(): CustomerDao
    abstract fun itemDao(): ItemDao


    companion object {
        @Volatile
        private var INSTANCE: InvoiceDatabase? = null

        fun getInstance(): InvoiceDatabase {
            return INSTANCE ?: synchronized(InvoiceDatabase::class) {
                val instance = buildDatabase()
                INSTANCE = instance
                instance //Uno es nullable y otro no lo es
            }
        }

        private fun buildDatabase(): InvoiceDatabase {
            return Room.databaseBuilder(
                Locator.requireApplication, InvoiceDatabase::class.java, "Invoice"
            ).fallbackToDestructiveMigration().allowMainThreadQueries()
                //y creamos un objeto de esta clase para convertir de un tipo a otro.
                .addTypeConverter(AccountIdTypeConverter())
                .addTypeConverter(EmailTypeConverter())
                .addTypeConverter(TaskStatusConverter())
                .addTypeConverter(TaskTypeConverter())
                .addTypeConverter(TaskIdTypeConverter())
                .addTypeConverter(CustomerIdTypeConverter())
                .addTypeConverter(InstantConverter())
                .addTypeConverter(InvoiceIdTypeConverter())
                .addTypeConverter(InvoiceStatusTypeConverter())
                .addTypeConverter(ItemIdTypeConverter())
                .addTypeConverter(ItemTypeConverter())
                .addTypeConverter(ItemVatTypeConverter())
                .addTypeConverter(UriTypeConverter())
                //.addTypeConverter(PhotoTypeConverter())
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

            applicationScope.launch(Dispatchers.IO) {
                populateDatabase()

            }
        }

        private suspend fun populateDatabase() {
            populateUsers()
            populateCustomer()
            populateItem()
            populateTask()
            populateInvoice()
            populateLineItem()
        }


        private fun populateUsers() {

            getInstance().let { invoiceDatabase ->
                invoiceDatabase.userDao().insert(
                    User("Alejandro", "abc@hotmail.es")
                )
                invoiceDatabase.userDao().insert(
                    User("Cristian", "rim@hotmail.es")
                )
                invoiceDatabase.userDao().insert(
                    User("Sergio", "123cab@hotmail.es")
                )
                invoiceDatabase.userDao().insert(
                    User("Jessica", "paella@hotmail.com")
                )
                invoiceDatabase.userDao().insert(
                    User("Pedro", "op@hotmail.es")
                )
                invoiceDatabase.userDao().insert(
                    User("Carlos", "mesa@gmail.com")
                )
            }
        }

        private fun populateCustomer() {
            var customId = 1;

            getInstance().let {
                it.customerDao().insert(
                    Customer(
                        CustomerId(customId++),
                        "Mr.Kiwi",
                        Email("kiwi@example.com"),
                        "+64 21 123 456",
                        "Auckland",
                        "Main Street, 123",

                        photo = Locator.getResourceUri(R.drawable.kiwituxedo)
                    )
                )

                it.customerDao().insert(
                    Customer(
                        CustomerId(customId++),
                        "Maria Schmidt",
                        Email("schmidt@example.com"),
                        "+49 123456789",
                        "Berlín",
                        "Kurfürstendamm, 123",
                        photo = Locator.getResourceUri(R.drawable.elephantuxedo)

                    )
                )

                it.customerDao().insert(
                    Customer(
                        CustomerId(customId++),
                        "Alejandro López",
                        Email("cebolla@example.com"),
                        photo = Locator.getResourceUri(R.drawable.cbotuxedo)
                    )
                )

                it.customerDao().insert(
                    Customer(
                        CustomerId(customId),
                        "Zariel García",
                        Email("garc@example.com"),
                        "+34 687223344",
                        "Valencia",
                        "Avenida Reino de Valencia, 789",
                        photo = Locator.getResourceUri(R.drawable.kangorutuxedo)
                    )
                )
            }
        }

        private fun populateInvoice() {
            getInstance().invoiceDao().insert(
                Invoice(
                    id = InvoiceId(1),
                    customerId = CustomerId(1),
                    number = InvoiceProvider.giveNumberInvoice(),
                    status = InvoiceStatus.PENDIENTE,
                    issuedDate = Instant.now(),
                    dueDate = Instant.now().plus(Duration.ofDays(30)),
                    lineItems = listOf(
                        LineItem(
                            InvoiceId(1),
                            ItemId(1),
                            1,
                            3.50,
                            5,
                        ),
                        LineItem(
                            InvoiceId(1),
                            ItemId(2),
                            1,
                            34.85, 21
                        ),

                        )
                )
            )
        }

        private fun populateLineItem() {
            getInstance().lineItemDao().insert(
                LineItem(
                    InvoiceId(1),
                    ItemId(1),
                    1,
                    2.4,
                    21,
                ),
            )
            getInstance().lineItemDao().insert(
                LineItem(
                    InvoiceId(1),
                    ItemId(2),
                    1,
                    4.56, 21
                ),
            )
        }


        private fun populateItem() {
            var itemId = 1;
            val itemDao = getInstance().itemDao()
            itemDao.insert(
                Item(
                    ItemId(itemId++), ItemType.PRODUCT,
                    VatItemType.TWENTYONE,
                    "Pizza",
                    2.52,
                    "Producto sección precocinados",
                    R.drawable.pizza
                )
            )
            itemDao.insert(
                Item(
                    ItemId(itemId++),
                    ItemType.SERVICE,
                    VatItemType.TWENTYONE,
                    "Repartir productos",
                    34.85, photo = R.drawable.servicio
                )
            )
            itemDao.insert(
                Item(
                    ItemId(itemId++), ItemType.PRODUCT,
                    VatItemType.TWENTYONE,
                    "Leche",
                    1.20,
                    "Producto sección lacteos",
                    R.drawable.leche,
                )
            )
            itemDao.insert(
                Item(
                    ItemId(itemId++),
                    ItemType.SERVICE,
                    VatItemType.TWENTYONE,
                    "Reponer productos",
                    29.43, photo = R.drawable.servicio
                )
            )
            itemDao.insert(
                Item(
                    ItemId(itemId++), ItemType.PRODUCT,
                    VatItemType.TWENTYONE,
                    "Manzana",
                    0.42,
                    "Producto sección fruta",
                    R.drawable.manzana
                )
            )
            itemDao.insert(
                Item(
                    ItemId(itemId), ItemType.PRODUCT,
                    VatItemType.FIVE,
                    "Pan de espelta",
                    0.92,
                    "Producto sección panadería",
                    R.drawable.panespelta
                )
            )
        }


        private fun populateTask() {
            var taskId = 1;

            getInstance().taskDao().insert(
                Task(
                    TaskId(taskId++),
                    CustomerId(2),
                    "Hacer la presentación",
                    TypeTask.LLAMAR,
                    TaskStatus.PENDIENTE,
                    "Preparar la presentación para la reunión de ventas",
                    Instant.parse("2023-11-15T00:00:00Z"),
                    Instant.parse("2023-11-15T00:00:00Z")
                )
            )
            getInstance().taskDao().insert(
                Task(
                    TaskId(taskId++),
                    CustomerId(2),
                    "Completar informe",
                    TypeTask.LLAMAR,
                    TaskStatus.PENDIENTE,
                    "Finalizar el informe mensual de ventas y enviarlo al cliente",
                    Instant.parse("2023-11-15T00:00:00Z"),
                    Instant.parse("2023-11-15T00:00:00Z"),
                )
            )
            getInstance().taskDao().insert(
                Task(
                    TaskId(taskId++),
                    CustomerId(3),
                    "Entrenamiento en línea",
                    TypeTask.VISITA,
                    TaskStatus.VENCIDA,
                    "Participar en el curso de desarrollo web en línea",
                    Instant.parse("2023-11-15T00:00:00Z"),
                    Instant.parse("2023-11-15T00:00:00Z"),
                )
            )
            getInstance().taskDao().insert(
                Task(
                    TaskId(taskId++),
                    CustomerId(4),
                    "Poblar la base de datos",
                    TypeTask.VISITA,
                    TaskStatus.VENCIDA,
                    "Participar en el proyecto",
                    Instant.parse("2023-11-15T00:00:00Z"),
                    Instant.parse("2023-11-15T00:00:00Z"),
                )
            )

        }
    }
}