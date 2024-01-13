package com.moronlu18.account.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.moronlu18.account.usecase.SignUpState
import com.moronlu18.account.usecase.SignUpViewModel
import com.moronlu18.accounts.enum_entity.TypePerfil
import com.moronlu18.accounts.enum_entity.TypeUser
import com.moronlu18.accounts.entity.User
import com.moronlu18.accountsignin.R
import com.moronlu18.accountsignin.databinding.FragmentAccountSignUpBinding

class SignUpFragment : Fragment() {

    private var _binding: FragmentAccountSignUpBinding? = null

    private val binding get() = _binding!!

    private val viewModel: SignUpViewModel by viewModels()


    private var onSuccessHandle = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentAccountSignUpBinding.inflate(inflater, container, false)

        binding.viewmodel = this.viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    /**
     * Se utiliza esta función para inicializar los componentes que se han creado ya en la función onCreateView
     * requiredContext() utiliza el de la clase base, this siempre en java.
     * la razón de context
     */

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*
        //1. Crear colección de datos
        val itemList = arrayListOf("Private", "Public", "Empty")
        //2. Crear Adapter
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, itemList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)*/

        //initSpinner()

        val listener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, //El adapterview es el listener que lo usaremos en este caso.
                view: View?,
                position: Int,
                id: Long
            ) {

                val profile =
                    parent?.adapter?.getItem(position) //Me devuelva el elemento seleccionado con los parametros de arriba
                Toast.makeText(requireActivity(), "Elemento pulsado $profile", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }


        }

        //binding.tietEmailSignIn.addTextChangedListener
        with(binding.spProfile) {
            this.adapter = adapter
            setSelection(2)
            onItemSelectedListener = listener
            onItemSelectedListener = null
        }
        viewModel.getState().observe(viewLifecycleOwner, Observer {
            when (it) {
                SignUpState.NameEmptyError -> setNameEmptyError()
                SignUpState.EmailEmptyError -> setEmailEmptyError()
                SignUpState.EmailFormatError -> setEmailFormatError()
                SignUpState.PasswordEmptyError -> setPasswordEmptyError()
                SignUpState.PasswordNotEquals -> setPasswordEqualsError()
                is SignUpState.Loading -> showProgressbar(it.value)
                is SignUpState.AuthencationError -> setEmailEquals(it.message)
                is SignUpState.OnSuccess -> {
                    if (!onSuccessHandle) {
                        onSuccess(it.user)
                        onSuccessHandle = true
                    }
                }
            }
        })

        binding.tietNameSignUp.addTextChangedListener(SignUpTextWatcher(binding.tietNameSignUp))
        binding.tietEmailSignUp.addTextChangedListener(SignUpTextWatcher(binding.tietEmailSignUp))
        binding.tietPasswordSignUp.addTextChangedListener(SignUpTextWatcher(binding.tietPasswordSignUp))
        binding.tietConfirmPasswordSignUp.addTextChangedListener(SignUpTextWatcher(binding.tietConfirmPasswordSignUp))

        binding.btnListado.setOnClickListener {

            findNavController().navigate(R.id.action_signUpFragment_to_userListFragment2)
        }

    }


    private fun setNameEmptyError() {
        binding.tilNameSignUp.error = "El campo Nombre está vacío"
        binding.tietNameSignUp.requestFocus()
    }

    private fun setEmailEmptyError() {
        binding.tilEmailSignUp.error =
            getString(R.string.errEmailEmpty)
        binding.tietEmailSignUp.requestFocus()
    }

    private fun setEmailEquals(message: String) {
        binding.tilEmailSignUp.error = message
        binding.tietEmailSignUp.requestFocus()
    }


    private fun setEmailFormatError() {
        binding.tilEmailSignUp.error = "Formato de email no válido"
        binding.tietEmailSignUp.requestFocus()
    }

    private fun setPasswordEmptyError() {

        if (binding.tietPasswordSignUp.text.toString().isBlank()) {
            binding.tilPasswordSignUp.error = "Password vacío"
            binding.tietPasswordSignUp.requestFocus()

        } else {
            binding.tilConfirmPasswordSignUp.error = "Password vacío"
            binding.tietConfirmPasswordSignUp.requestFocus()
        }
    }

    private fun setPasswordEqualsError() {
        //binding.tilPasswordSignUp.error = "Las contraseñas no coinciden"
        Snackbar.make(requireView(), "Las contraseñas no coinciden", Snackbar.LENGTH_LONG).show()
        binding.tietPasswordSignUp.text = null
        binding.tietConfirmPasswordSignUp.text = null
        binding.tietPasswordSignUp.requestFocus()
    }


    private fun onSuccess(user: User) {

        viewModel.addUserDirect(user)
        Toast.makeText(requireContext(), "Se ha añadido al repositorio", Toast.LENGTH_SHORT).show()

        findNavController().navigate(R.id.action_signUpFragment_to_userListFragment2)
    }


    open inner class SignUpTextWatcher(private val textView: TextView) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            when (textView.id) {

                R.id.tietNameSignUp -> {
                    binding.tilNameSignUp.error = null
                }

                R.id.tietEmailSignUp -> {
                    binding.tilEmailSignUp.error = null
                }

                R.id.tietPasswordSignUp -> {
                    binding.tilPasswordSignUp.error = null
                }

                R.id.tietConfirmPasswordSignUp -> {
                    binding.tilConfirmPasswordSignUp.error = null
                }
            }
        }
    }


    private fun showProgressbar(value: Boolean) {
        if (value)
            findNavController().navigate(R.id.action_signUpFragment_to_fragmentProgressDialog)
        else {
            //findNavController().popBackStack()
            findNavController().popBackStack()
        }
    }

    private fun chooseUserType(): TypeUser {
        return when (binding.spType.selectedItemId) {
            0L -> TypeUser.USER
            1L -> TypeUser.ADMIN
            2L -> TypeUser.CLIENT
            else -> {
                TypeUser.GUEST
            }
        }
    }


    override fun onResume() {
        super.onResume()
        onSuccessHandle = false
    }

    private fun choosePerfilType(): TypePerfil {
        return when (binding.spProfile.selectedItemId) {
            0L -> TypePerfil.EMPTY
            1L -> TypePerfil.PRIVATE
            else -> {
                TypePerfil.PUBLIC
            }
        }
    }


    private fun initSpinner() {

        /*val spinner: Spinner = binding.spinRepositoryUser
        val adapterSpinner = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            viewModel.giveListUserSignUp().toTypedArray()
        )

        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapterSpinner*/
    }
}