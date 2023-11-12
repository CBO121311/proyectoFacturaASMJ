package com.moronlu18.accountsignin.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.moronlu18.accountsignin.R
import com.moronlu18.accountsignin.databinding.FragmentAccountSignInBinding



class AccountSignIn : Fragment() {

    private var _binding: FragmentAccountSignInBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAccountSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       binding.btSigIn.setOnClickListener{
          //findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }
        binding.btListUser.setOnClickListener{
            findNavController().navigate(com.moronlu18.invoice.R.id.action_accountSignInFragment_to_as_userListFragment) //Crear esta acción
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}