package com.moronlu18.invoice.ui

import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.moronlu18.invoice.R
import com.moronlu18.invoice.databinding.ActivityMainBinding
import com.moronlu18.invoice.utils.showToast

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    //Todas las propiedades son publicas
    //Propiedades de acceso al botón flotante de la Activity principal y b. herramientas


    //Tiene acceso? Si, pero como se añadió otra copa, hay que bajar el nivel.
    //binding.fab
    val fab: FloatingActionButton get() = binding.content.fab
    val toolbar: Toolbar get() = binding.content.toolbar
    val drawer: DrawerLayout get() = binding.drawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Todo
        //Sustituir la AppBar por defecto por el widget Toolbar de nuestro layout
        setSupportActionBar(binding.content.toolbar)

        //OPCION 1:
        //Habilitar el icono Home(hamburguesa) dentro de la barra AppBar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Lo que hace este es poner el icono hamburguesa [---]
        //que tiene un id android.R.id.home
        supportActionBar?.setHomeButtonEnabled(true) //todo comentamos esto de nuevo.
        //supportActionBar?.setHomeAsUpIndicator(android.R.drawable.ic_menu_info_details)



        //OPCION 2:
        //1. Métodos que permite acceder al controlador del Grafo de navegación
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        navController = navHostFragment.navController


        //Yo le digo que se configure la barra de navegación con este grafo
        //2. Se crea la configuración de la App Bar con el control de la apertura y cierre del DrawerLayout
        appBarConfiguration =
            AppBarConfiguration.Builder(navController.graph).setOpenableLayout(binding.drawerLayout)
                .build()

        //3. Que sincronice el DrawerLayout con la AppBar
        NavigationUI.setupWithNavController(
            binding.content.toolbar, navController, appBarConfiguration
        )

        //La appbar es responsable del drawerlayout, y quiero que sea responsable del abrir y el cierre del Drawerlayout



        //appBarConfiguration = AppBarConfiguration(navController.graph)
        //setupActionBarWithNavController(navController, appBarConfiguration)




        //Si utilizo estas tres últimos líneas de código puedo resetear la barra de navegación.


        /*val navController = findNavController(R.id.nav_host_fragment_content_main) as NavHostController
        navController = navHostFragment.navController*/


        binding.content.fab.setOnClickListener { view ->
            Snackbar.make(
                view,
                "No me dejes así, pon una función o hazla no visible <_<",
                Snackbar.LENGTH_LONG
            )
                .setAction("Action", null).show()
        }

        //Configurar evento click del menu Nav_View.
        setupNavigationView()

        //navController = findNavController(R.id.nav_host_fragment_content_main)
    }

    /**
     * Implementar el listener de las opciones del menú del componente Nav_view
     */
    private fun setupNavigationView(){
     binding.navView.setNavigationItemSelectedListener { item->
         when(item.itemId){
             R.id.action_invoice ->{
                 //findNavController.navigate(R.id.action_mainFragment_to_nav_graph_customer)
                 navController.navigate(R.id.action_mainFragment_to_nav_graph_customer)
                 showToast("He pulsado Invoice")
             }

             else -> {
                 showToast("Opción invalida")
             }
         }
         binding.drawerLayout.closeDrawer(GravityCompat.START)
         true
     }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //Añadido por mi.
        //binding.fab.visibility = View.VISIBLE

        return when (item.itemId) {
            R.id.action_settings -> {
                //La navegación se realiza directamente utilizando el id del fragment.
                navController.navigate(R.id.settingsFragment)
                true
            }
            android.R.id.home->{
                binding.drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }


    /**
     * Se sobreescribe el método que gestiona la pulsación del botón Back
     */
    //Solo si la caja está abierta es cuando lo cerramos.
    override fun onBackPressed() {
        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        else //dejamos que el SO haga su función
        super.onBackPressed()
    }
}