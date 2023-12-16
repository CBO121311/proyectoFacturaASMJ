package com.moronlu18.account.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.moronlu18.account.usecase.SignInState
import com.moronlu18.account.usecase.SignInViewModel
import com.moronlu18.account.usecase.TAG
import com.moronlu18.accountsignin.R
import com.moronlu18.accountsignin.databinding.FragmentAccountSignInBinding


class SignInFragment : Fragment() {

    private var _binding: FragmentAccountSignInBinding? =
        null
    private val binding get() = _binding!!


    private val viewModel: SignInViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAccountSignInBinding.inflate(inflater, container, false)

        binding.viewmodel = this.viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getState().observe(viewLifecycleOwner, Observer {
            when (it) {
                SignInState.EmailEmptyError -> setEmailEmptyError()
                SignInState.PasswordEmptyError -> setPasswordEmptyError()

                //is Es un data class.
                is SignInState.AuthencationError -> showMessage(it.message)
                is SignInState.Loading -> showProgressbar(it.value)
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


        /*binding.btSignUpInSignIn.setOnClickListener {
            //
            findNavController().navigate(R.id.action_accountSignIn_to_accountSignUp)
        }*/
    }

    /**
     * Mostar un progressbar en el comienzo de una operación larga como es una consulta a la base de datos.
     * Firebase o bien ocultar cuando la operación ha terminado.
     */
    private fun showProgressbar(value: Boolean) {
        if (value)
            findNavController().navigate(R.id.action_accountSignIn_to_fragmentProgressDialog)
        else {
            findNavController().popBackStack()
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
        val action =
            SignInFragmentDirections.actionAccountSignInToBaseFragmentDialog("Error", message)
        //Navegamos al dialog
        findNavController().navigate(action)

        Toast.makeText(requireActivity(), "Caso de éxito en el login", Toast.LENGTH_LONG).show()

    }

    /**
     * Función que muestra el error de Email Empty
     */
    private fun setEmailEmptyError() {

        binding.tilEmailSignIn.error =
            getString(R.string.errEmailEmpty)
        binding.tilEmailSignIn.requestFocus()
    }

    /**
     * Función que muestra el error de Password
     */
    private fun setPasswordEmptyError() {
        binding.tilPassword.error =
            getString(R.string.errPasswordEmpty)
        binding.tilPassword.requestFocus()
    }

    private fun onSuccess() {
        val action =
            SignInFragmentDirections.actionAccountSignInToBaseFragmentDialog(
                "Éxito",
                "Login con exito"
            )
        findNavController().navigate(action)

        findNavController().popBackStack()
        findNavController().navigate(R.id.action_accountSignIn_to_userListFragment)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}