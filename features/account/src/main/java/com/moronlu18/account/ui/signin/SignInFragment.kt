package com.moronlu18.account.ui.signin

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.moronlu18.accounts.entity.Account
import com.moronlu18.accountsignin.R
import com.moronlu18.accountsignin.databinding.FragmentAccountSignInBinding


class SignInFragment : Fragment() {

    private var _binding: FragmentAccountSignInBinding? =
        null //viewBinding está toda la instancia de todos mis componentes.

    //Se inicializará posteriormente
    private val binding get() = _binding!!

    //private lateinit var viewModel: SignInViewModel
    private val viewModel: SignInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAccountSignInBinding.inflate(inflater, container, false)

        /*
        pasamos a la interfaz la instancia del viewModel para que actualice/recoge los valores
         del email y password automáticamente y se asociar el evento onCllick del botón a una función.


        si no decimos su ciclo de vida, no responderá a los fragmentos del ciclo de vida
        Recorda que cuando se destruye la vista, con el databinding se pasa unos datos.

        IMPORTANTE: Hay que establcer el Fragment/Activity vinculado al binding para actualizar
        los valores del Binding en base al ciclo de vida.
        */

        binding.viewmodel = this.viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        binding.btListUser.setOnClickListener {
            //findNavController().navigate(com.moronlu18.invoice.R.id.action_accountSignInFragment_to_as_userListFragment) //Crear esta acción
        }

        /*
          //ya no es necesario setOnClickListener porque lo hará validateCredentials

         binding.tietEmailSignIn.addTextChangedListener { SignInWatcher(tex) }

        //Tengo que pasarle un observador, una vez que tenga el parametro de entrada actualizado lo actualizar
        //viewLifecycleOwner es lo que observa (?)

        //el viewModel se ejecuta
              //viewModel.validateCredentials()
        */



        viewModel.getState().observe(viewLifecycleOwner, Observer {
            when (it) {
                SignInState.EmailEmptyError -> setEmailEmptyError()
                SignInState.PasswordEmptyError -> setPasswordEmptyError()

                //is Es un data class.
                is SignInState.AuthencationError -> showMessage(it.message)
                is SignInState.Loading -> showProgressbar(it.value)
                //is SignInState.Success -> showMessageCorrect(it.account!!);
                else -> onSuccess() //Todos los casos de uso tiene uno de éxito
            }
        })
        binding.tietEmailSignIn.addTextChangedListener(SignInWatcher(binding.tilEmailSignIn))
        binding.tietPassword.addTextChangedListener(SignInWatcher(binding.tilPassword))


        //Este código no es necesario ya que se implemente databinding
        binding.btListUser.setOnClickListener {
            findNavController().navigate(R.id.action_accountSignIn_to_userListFragment)
        }

        binding.btnLayout.setOnClickListener {
            //
            findNavController().navigate(R.id.action_accountSignIn_to_blankFragmentpRUEBA2)
        }


    }

    /**
     * Mostar un progressbar en el comienzo de una operación larga como es una consulta a la base de datos.
     * Firebase o bien ocultar cuando la operación ha terminado.
     */
    private fun showProgressbar(value: Boolean) {
        if (value)
            findNavController().navigate(R.id.action_accountSignIn_to_fragmentProgressDialog)
        else{
            findNavController().popBackStack()
        }
    }


    //Me interesa acceder a las propiedas y funciones de la clase externa

    //region Login Prueba
    open inner class Login(private val textView: TextView) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            //til.isErrorEnabled = false
            textView.error = null


            when (textView.id) {
                R.id.tietEmailSignIn -> {
                    binding.tilEmailSignIn.error = null
                    binding.tilPassword.isErrorEnabled = false
                }

                R.id.tietPassword -> binding.tilPassword.error = null

            }
        }
    }

    //endregion
    open inner class SignInWatcher(private val til: TextInputLayout) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            //til.isErrorEnabled = false
            til.error = null
        }
    }

    /**
     * Función que muestra al usuario un mensaje
     */

    private fun showMessage(message: String) {
        //Toast.makeText(requireContext(), "Mi primer MVVM $message", Toast.LENGTH_SHORT).show()
        val action =
            SignInFragmentDirections.actionAccountSignInToBaseFragmentDialog("Error", message)
        //Navegamos al dialog
        findNavController().navigate(action)

    }


    /**
     * Función que muestra al usuario un mensaje
     */

    private fun showMessageCorrect(cuenta: Account) {
        //Toast.makeText(requireContext(), "Mi primer MVVM $message", Toast.LENGTH_SHORT).show()
        val action =
            SignInFragmentDirections.actionAccountSignInToBaseFragmentDialog("Éxito", cuenta.email.toString())
        //Navegamos al dialog
        findNavController().navigate(action)

    }


    /**
     * Función que muestra el error de Email Empty
     */
    private fun setEmailEmptyError() {

        binding.tilEmailSignIn.error =
            getString(R.string.errEmailEmpty) //Yo puedo crear el valor de un fichero más en values.
        //El cursor del foco se coloca en el til que tiene el error
        binding.tilEmailSignIn.requestFocus()
    }

    /**
     * Función que muestra el error de Password
     */
    private fun setPasswordEmptyError() {
        binding.tilPassword.error =
            getString(R.string.errPasswordEmpty)
        /*
        Yo puedo crear el valor de un fichero más en values.
        El cursor del foco se coloca en el til que tiene el error
        */
        binding.tilPassword.requestFocus()
    }

    private fun onSuccess() {

        findNavController().navigate(R.id.action_accountSignIn_to_userListFragment)
        Toast.makeText(requireActivity(), "Caso de éxito en el login", Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

/*Es una función de suspensión no bloquea el hilo.
Una corrutina es un conjunto de instrucciones que hace un trabajo.
Que se hace en un hilo pero yo no lo creo
El hilo principal se duerme (Thread.sleep)
*/
