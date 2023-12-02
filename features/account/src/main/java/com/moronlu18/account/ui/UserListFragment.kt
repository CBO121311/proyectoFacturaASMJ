package com.moronlu18.account.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.moronlu18.account.adapter.UserAdapter
import com.moronlu18.account.usecase.UserListState
import com.moronlu18.account.usecase.UserListViewModel
import com.moronlu18.accounts.entity.User
import com.moronlu18.accountsignin.databinding.FragmentUserListBinding


class UserListFragment : Fragment(), UserAdapter.OnUserClick {

    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!

    //Esto lo da el proveedor por defecto
    private val viewmodel:UserListViewModel by viewModels()
    private lateinit var userAdapter:UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUserListBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpUserRecycler()


        //Creamos un observador
        viewmodel.getState().observe(viewLifecycleOwner, Observer {
            when(it){
                is UserListState.Loading -> showProgressBar(it.value)

                UserListState.NoDataError -> showNoDataError()
                is UserListState.Success -> onSuccess(it.dataset)
            }
        })
    }

    /**
     * Cuando el faragment se inicia debe pedir el lsitado de usarios al viewmodel. infreaestructura
     */


    override fun onStart() {
        super.onStart()
        viewmodel.getUserList()
    }

    //Cuando quiero que se adapte en tiempo de ejecución.
    //Que se cancele y se vuelva a dibujar (notificacionSetChange())

    /**
     * Función que contiene el listado de usuarios
     */

    //El adapter tiene los usuarios.
    private fun onSuccess(dataset:ArrayList<User>){
        //Desactivar la animación y visualizar el recyclerView
        hideNoDataError()

        //Si es éxito
        userAdapter.update(dataset)
    }

    private fun hideNoDataError() {
        println("hideNoData")
        binding.animationView.visibility = View.GONE
        binding.rvUser.visibility=View.VISIBLE
    }


    /**
     * Función que muestra el error de que no hay datos
     */
    private fun showNoDataError() {
        println("showNoDataErrror")
        binding.animationView.visibility = View.VISIBLE
        binding.rvUser.visibility=View.GONE //No ocupa espacio.

    }



    /**
     * Mostar el progressbar en la vista
     */
    private fun showProgressBar(value: Boolean) {
        println("showProgressBar")
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
        userAdapter = UserAdapter(this){
            //when(event){}

            Toast.makeText(requireContext(),"Usuario Seleccionado mediante lambda $it", Toast.LENGTH_LONG).show()
        }

        /*adapter = UserAdapter(UserRepository.dataSet, requireContext(),this){
            //when(event){}

            Toast.makeText(requireContext(),"Usuario Seleccionado mediante lambda $it", Toast.LENGTH_LONG).show()
        }*/

        //1. ¿Cómo quiero que se muestren los elementos de la lista?
        with(binding.rvUser) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            this.adapter =  userAdapter
        }
    }

    /**
     * Esta función se llama de forma asíncrona cuando el usuario sobre un elemento del recycleview.
     */

    override fun userClick(user: com.moronlu18.accounts.entity.User) {
        Toast.makeText(requireActivity(),"Pulsación corta en el usuario $user", Toast.LENGTH_LONG).show()
    }

    override fun userOnLongClick(user: com.moronlu18.accounts.entity.User) {
        Toast.makeText(requireActivity(),"Pulsación larga en el usuario $user", Toast.LENGTH_LONG).show()
    }


}