package com.moronlu18.account.ui.signin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.moronlu18.accountsignin.R
import com.moronlu18.accountsignin.databinding.FragmentAccountSignInBinding


class AccountSignIn : Fragment() {

    //Esto es llama SignInFragment

    private var _binding: FragmentAccountSignInBinding? =
        null //viewBinding está toda la instancia de todos mis componentes.

    //Se inicializará posteriormente
    private val binding get() = _binding!!

    //private lateinit var viewModel: SignInViewModel
    private val viewModel: SignInViewModel by viewModels()
    //private var activity:MainActivity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAccountSignInBinding.inflate(inflater, container, false)

        //pasamos a la interfaz la instancia del viewModel para que actualice/recoge los valores
        // del email y password automáticamente y se asociar el evento onCllick del botón a una función.
        binding.viewmodel = this.viewModel


        //si no decimos su ciclo de vida, no responderá a los fragmentos del ciclo de vida
        //Recorda que cuando se destruye la vista, con el databinding se pasa unos datos.

        //IMPORTANTE: Hay que establcer el Fragment/Activity vinculado al binding para actualizar
        //los valores del Binding en base al ciclo de vida.
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.validateCredentials() //ya no es necesario setOnClickListener porque lo hará validateCredentials

        binding.btListUser.setOnClickListener {
            //findNavController().navigate(com.moronlu18.invoice.R.id.action_accountSignInFragment_to_as_userListFragment) //Crear esta acción


        }

        //Tengo que pasarle un observador, una vez que tenga el parametro de entrada actualizado lo actualizar
        //viewLifecycleOwner es lo que observa (?)
        viewModel.getState().observe(viewLifecycleOwner, Observer {
            when (it) {
            SignInState.EmailEmptyError -> setEmailEmptyError()
                SignInState.PasswordEmptyError -> setPasswordEmptyError()
                else -> onSuccess() //Todos los casos de uso tiene uno de éxito
            }
        })

        //Este código no es necesario ya que se implemente databinding
        //binding.btSigIn.setOnClickListener{
        //findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        //}


    }

    /**
     * Función que muestra el error de Email Empty
     */
    private fun setEmailEmptyError() {
        binding.tilEmailSignIn.error = getString(com.moronlu18.accountsignin.R.string.errEmailEmpty) //Yo puedo crear el valor de un fichero más en values.
        //El cursor del foco se coloca en el til que tiene el error
        binding.tilEmailSignIn.requestFocus()
    }
    /**
     * Función que muestra el error de Password
     */
    private fun setPasswordEmptyError() {
        binding.tilPassword.error = getString(R.string.errPasswordEmpty) //Yo puedo crear el valor de un fichero más en values.
        //El cursor del foco se coloca en el til que tiene el error
        binding.tilPassword.requestFocus()
    }

    private fun onSuccess(){

        Toast.makeText(requireActivity(),"Caso de éxito en el login", Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}