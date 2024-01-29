package com.moronlu18.invoice.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.moronlu18.invoice.R
import com.moronlu18.invoice.databinding.ActivityMainBinding

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Todo
        //Sustituir la AppBar por defecto por el widget Toolbar de nuestro layout
        setSupportActionBar(binding.content.toolbar)
        //Habilitar el icono Home(hamburguesa) dentro de la barra AppBar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(android.R.drawable.ic_menu_info_details)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        navController = navHostFragment.navController

        //Yo le digo que se configure la barra de navegación con este grafo
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

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

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }


}