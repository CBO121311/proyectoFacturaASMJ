package com.moronlu18.account.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.moronlu18.account.adapter.UserAdapter
import com.moronlu18.accounts.repository.UserRepository
import com.moronlu18.accountsignin.databinding.FragmentUserListBinding


class UserListFragment : Fragment(), UserAdapter.OnUserClick {

    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!

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
    }

    /**
     * Función que inicializa el RecyclerView que muestra el listado de usuarios de la aplicación
     */
    private fun setUpUserRecycler() {

        /*Añadimos el listener
        var adapter = UserAdapter(UserRepository.dataSet, requireContext(),this)

        El abstracto, recoge un usuario y puedo usar el view.
        Otra manera --> Abramos llaves

        todo ¿Esto está mal? Ya que pregunta al repositorio  var adapter = UserAdapter(null, requireContext(),this)
        todo Ya que el que le facilita es el usecase. Así se inicia a null
        var adapter = UserAdapter(null, requireContext(),this){*/
        var adapter = UserAdapter(UserRepository.dataSet, requireContext(),this){
            //when(event){}

            Toast.makeText(requireContext(),"Usuario Seleccionado mediante lambda $it", Toast.LENGTH_LONG).show()
        }

        //1. ¿Cómo quiero que se muestren los elementos de la lista?
        with(binding.rvUser) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            this.adapter = adapter
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

    override fun onStart() {
        super.onStart()
        //viewmodel.getList()
    }
}