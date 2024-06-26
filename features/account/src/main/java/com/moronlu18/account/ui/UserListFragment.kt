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
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.moronlu18.account.adapter.UserAdapter
import com.moronlu18.account.usecase.UserListViewModel
import com.moronlu18.data.account.User
import com.moronlu18.accountsignin.R
import com.moronlu18.accountsignin.databinding.FragmentUserListBinding
import com.moronlu18.invoice.ui.MainActivity
import com.moronlu18.invoice.utils.Utils


//Aquí te doy permiso para el menu
//Basicamente es un contrato para que yo te considere mi proveedor de menú.
class UserListFragment : Fragment(), UserAdapter.OnUserClick, MenuProvider {

    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserListViewModel by viewModels()
    private lateinit var userAdapter: UserAdapter

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
        //setUpFab()

        //Función que personal el menu de la Toolbar
        setUpToolbar()

        setUpUserRecycler()


        //Creamos un observador



        /*viewModel.getState().observe(viewLifecycleOwner, Observer {
            when (it) {
                is UserListState.Loading -> showProgressBar(it.value)
                UserListState.NoDataError -> showNoDataError()
                is UserListState.Success -> onSuccess()
            }
        })*/

        //Este OBSERVADOR de un Livedata se ejecutará SIEMPRE que haya cambios en la tabla
        //user de la base datos. El adapter se actualiza a través del
        // COMPARATOR del adaptar


        //Mira la diferencia. El submit es algo del propio de ListAdapter
        viewModel.allUser.observe(viewLifecycleOwner) {
            it.let { userAdapter.submitList(it) }
        }
    }


    /**
     * Cuando el fragment se inicia debe pedir el lsitado de usarios al viewmodel. infreaestructura
     */

    override fun onStart() {
        super.onStart()
        viewModel.getUserList()
    }

    /**
     * Función que contiene el listado de usuarios
     */

    //El adapter tiene los usuarios.
    private fun onSuccess() {
        //Desactivar la animación y visualizar el recyclerView
        hideNoDataError()



        //Si es éxito
        //userAdapter.update(dataset)
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

    private fun setUpUserRecycler() {
        //Crear el constructor primario
        userAdapter = UserAdapter(this) {
            Toast.makeText(requireContext(), "Usuario Seleccionado mediante lambda $it", Toast.LENGTH_LONG).show()
        }

        //1. ¿Cómo quiero que se muestren los elementos de la lista?
        with(binding.rvUser) {
            layoutManager = LinearLayoutManager(requireContext())
            //El recyclerview es dinámico ya que utilizamos listadapter y se modifica el número
            //de elementos. Se debe quitar setHasFixedSize();
            //setHasFixedSize(true) //No se actualiza el recycler view
            this.adapter = userAdapter
        }
    }

    /**
     * Esta función se llama de forma asíncrona cuando el usuario sobre un elemento del recycleview.
     */

    override fun userClick(user: User) {
        //Toast.makeText(requireActivity(), "Pulsación corta en el usuario $user", Toast.LENGTH_LONG).show()

        //Todos los módulos tienen acceso al App.

        //La diferencia es que dentro de un objeto lo de Util y no
        //requireActivity().showToast("Pulsación corta en el usuario $user")

        Utils.showToast(requireContext(),"Pulsación corta a través del $user")

    }

    override fun userOnLongClick(user: User) {
        //Toast.makeText(requireActivity(), "Pulsación larga en el usuario $user", Toast.LENGTH_LONG).show()
        showConfirmationDialog(user)
    }


    /**
     * AlertDialog para borrar
     */

    private fun showConfirmationDialog(user: User) {

        AlertDialog.Builder(requireContext())
            .setTitle("Confirmación")
            .setMessage("¿Estás seguro de que quieres eliminar a ${user.name}?")
            .setPositiveButton("Sí") { _, _ ->
                viewModel.delete(user)

            }
            .setNegativeButton("No", null)
            .show()
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
                //Ordenado por nombre
                //viewModel.sortNatural()
                userAdapter.sort()
                viewModel.getUserList()
                return  true
            }

            R.id.action_refresh ->{
                //Ordenador por email
                //viewModel.sortPreestablecido()
                viewModel.getUserList()
                return true
            }
            else -> false
        }
    }

    /**
     * Esta función personaliza el comportamiento de la Toolbar de la Activity
     */
    private fun setUpToolbar() {
        //Modismo Apply de Kotlin
        (requireActivity() as? MainActivity)?.toolbar?.apply {
            visibility = View.VISIBLE
        }

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


    /**
     * Esta función personaliza el comportamiento del botón flotante de la activity
     */
    private fun setUpFab() {

        //Todo quitar comentario cuando pueda, es lo del float button

        /* (requireActivity() as? MainActivity)?.fab?.apply {
            visibility = View.VISIBLE
            setOnClickListener { view ->
                //Aquí la acción del listener
                Snackbar.make(view, "Soy el Fragment", Snackbar.LENGTH_LONG).show()
            }
        }*/


        //Otra forma de hacerlo.
        /*fab?.visibility = View.VISIBLE
        fab?.setOnClickListener { view->
            //Aquí la acción del listener
            Snackbar.make(view,"Soy el Fragment",Snackbar.LENGTH_LONG).show()
        }*/


        //Un modismo es para simplifcar código. Así que el de arriba no sirve.
    }

}