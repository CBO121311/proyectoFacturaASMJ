package com.moronlu18.account.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.moronlu18.account.adapter.UserAdapter
import com.moronlu18.account.usecase.UserListState
import com.moronlu18.account.usecase.UserListViewModel
import com.moronlu18.accounts.entity.User
import com.moronlu18.accountsignin.R
import com.moronlu18.accountsignin.databinding.FragmentUserListBinding
import com.moronlu18.invoice.ui.MainActivity


//Aquí te doy permiso para el menu
//Basicamente es un contrato para que yo te considere mi proveedor de menú.
class UserListFragment : Fragment(), UserAdapter.OnUserClick, MenuProvider {

    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!

    //Esto lo da el proveedor por defecto
    private val viewModel: UserListViewModel by viewModels()
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUserListBinding.inflate(inflater, container, false)

        binding.viewmodel = this.viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Funcion que personaliza el botón flotante
        setUpFab()

        //Función que personal el menu de la Toolbar
        setUpToolbar()

        setUpUserRecycler()


        //Creamos un observador
        viewModel.getState().observe(viewLifecycleOwner, Observer {
            when (it) {
                is UserListState.Loading -> showProgressBar(it.value)
                UserListState.NoDataError -> showNoDataError()
                is UserListState.Success -> onSuccess(it.dataset)
            }
        })
    }


    /**
     * Cuando el fragment se inicia debe pedir el lsitado de usarios al viewmodel. infreaestructura
     */


    override fun onStart() {
        super.onStart()
        viewModel.getUserList()
    }

    //Cuando quiero que se adapte en tiempo de ejecución.
    //Que se cancele y se vuelva a dibujar (notificacionSetChange())

    /**
     * Función que contiene el listado de usuarios
     */

    //El adapter tiene los usuarios.
    private fun onSuccess(dataset: ArrayList<User>) {
        //Desactivar la animación y visualizar el recyclerView
        hideNoDataError()

        //Si es éxito
        userAdapter.update(dataset)
    }

    private fun hideNoDataError() {
        println("hideNoData")
        binding.animationView.visibility = View.GONE
        binding.rvUser.visibility = View.VISIBLE
    }


    /**
     * Función que muestra el error de que no hay datos
     */
    private fun showNoDataError() {
        println("showNoDataError")
        binding.animationView.visibility = View.VISIBLE
        binding.rvUser.visibility = View.GONE //No ocupa espacio.

    }


    /**
     * Mostar el progressbar en la vista
     */
    private fun showProgressBar(value: Boolean) {
        if (value) {
            findNavController().navigate(R.id.action_userListFragment_to_fragmentProgressDialog)
        } else {
            findNavController().popBackStack()
        }
    }

    /**
     * Función que inicializa el RecyclerView que muestra el listado de usuarios de la aplicación
     */

    //lateinit val al adapter porque después se reasigna los datos.
    private fun setUpUserRecycler() {

        /*Añadimos el listener
        var adapter = UserAdapter(UserRepository.dataSet, requireContext(),this)

        El abstracto, recoge un usuario y puedo usar el view.
        Otra manera --> Abramos llaves

        todo ¿Esto está mal? Ya que pregunta al repositorio  var adapter = UserAdapter(null, requireContext(),this)
        todo Ya que el que le facilita es el usecase. Así se inicia a null
        var adapter = UserAdapter(null, requireContext(),this){*/


        //Crear el constructor primerario
        userAdapter = UserAdapter(this) {
            //when(event){}

            Toast.makeText(
                requireContext(),
                "Usuario Seleccionado mediante lambda $it",
                Toast.LENGTH_LONG
            ).show()
        }

        /*adapter = UserAdapter(UserRepository.dataSet, requireContext(),this){
            //when(event){}

            Toast.makeText(requireContext(),"Usuario Seleccionado mediante lambda $it", Toast.LENGTH_LONG).show()
        }*/

        //1. ¿Cómo quiero que se muestren los elementos de la lista?
        with(binding.rvUser) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            this.adapter = userAdapter
        }
    }

    /**
     * Esta función se llama de forma asíncrona cuando el usuario sobre un elemento del recycleview.
     */

    override fun userClick(user: com.moronlu18.accounts.entity.User) {
        Toast.makeText(requireActivity(), "Pulsación corta en el usuario $user", Toast.LENGTH_LONG)
            .show()
    }

    override fun userOnLongClick(user: com.moronlu18.accounts.entity.User) {
        Toast.makeText(requireActivity(), "Pulsación larga en el usuario $user", Toast.LENGTH_LONG)
            .show()
    }

    /**
     * Esta función personaliza el comportamiento del botón flotante de la activity
     */
    private fun setUpFab() {
        //requireActivity te devuelve el contenedor padre.
        //Los fragment siempre viene de un activity por eso.
        //val fab = (requireActivity() as? MainActivity)?.fab


        //Apply simplifica la inicialización del objeto fab, son modismos

        //Todo quitar comentario cuando pueda
        /* (requireActivity() as? MainActivity)?.fab?.apply {
            visibility = View.VISIBLE
            setOnClickListener { view ->
                //Aquí la acción del listener
                Snackbar.make(view, "Soy el Fragment", Snackbar.LENGTH_LONG).show()
            }
        }*/


        /*fab?.visibility = View.VISIBLE
        fab?.setOnClickListener { view->
            //Aquí la acción del listener
            Snackbar.make(view,"Soy el Fragment",Snackbar.LENGTH_LONG).show()
        }*/


        //Un modismo es para simplifcar código. Así que el de arriba no sirve.
    }

    /**
     * Esta función personaliza el comportamiento de la Toolbar de la Activity
     */
    //Si quieres ser proveedor de menú.
    private fun setUpToolbar() {
        //Modismo Apply de Kotlin
        (requireActivity() as? MainActivity)?.toolbar?.apply {
            visibility = View.VISIBLE
        }
        //Es un menu que añade a la clase Provider

        val menuHost: MenuHost = requireActivity()

        //El menú está condicionado al ciclo de vida a ese provider.
        //Solo va a estar visible al ciclo de vida a ese fragment
        //viewLifecycleOwner = ciclo de vida del fragmento.
        //Lifecycle.State.RESUMED es opcional, solo se utiliza el resume
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


    /**
     * Se añade las opciones del menu definidas en R.menu.menu_list_user
     * al menú principal
     */
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_list_user,menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId){
            R.id.action_sort-> {


                viewModel.sortPreestablecido()
                //userAdapter.sort()
                //userAdapter.sortabc()
                //viewModel.getUserList()


                viewModel.getUserList()
                return  true
            }

            R.id.action_refresh ->{
                viewModel.sortNatural()

                //viewModel.getUserList()
                //userAdapter.notifyDataSetChanged()

                viewModel.getUserList()
                return true
            }

            else -> false
        }

    }


}