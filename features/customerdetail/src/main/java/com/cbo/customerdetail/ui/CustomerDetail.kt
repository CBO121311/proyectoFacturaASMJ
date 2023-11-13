package com.cbo.customerdetail.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.cbo.customerdetail.ui.CustomerDetailArgs
//import com.cbo.customerlist.ui.CustomerListArgs
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.moronlu18.customerdetail.databinding.FragmentCustomerDetailBinding

class CustomerDetail : Fragment() {

    private val args: CustomerDetailArgs by navArgs()
    //private val idCliente = args.customerPrueba
    //private val args: CustomerListArgs by NavArgs()
    //private val cliente = args.cliente
    private var _binding: FragmentCustomerDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //val args: FragmentCustomerDetailBinding by navArgs<>()
        // Inflate the layout for this fragment

        /*binding.customerDetailTvNameCustomer.text = cliente.name;
        binding.customerDetailTvEmailCustomer.text = cliente.email;
        binding.customerDetailTvPhoneCustomer.text = cliente.phone;
        binding.customerDetailTvCity.text = cliente.city;
        binding.customerDetailTvAddressCustomer.text = cliente.address;*/

        _binding = FragmentCustomerDetailBinding.inflate(inflater, container, false)

        //binding.customerDetailTvAddressCustomer.text = idCliente
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}