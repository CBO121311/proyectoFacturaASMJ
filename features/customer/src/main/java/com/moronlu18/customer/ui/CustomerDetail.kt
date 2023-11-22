package com.moronlu18.customer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.moronlu18.accounts.entity.Clientes
import com.moronlu18.accounts.repository.ClientesProvider
import com.moronlu18.customercreation.databinding.FragmentCustomerDetailBinding


class CustomerDetail : Fragment() {

    //private val args: CustomerDetailArgs by navArgs()
    //private val idCliente = args.customerPrueba
    private val args: CustomerDetailArgs by navArgs()

    //private val cliente = args.cliente
    private var _binding: FragmentCustomerDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentCustomerDetailBinding.inflate(inflater, container, false)

        val idcliente: Int = args.idcliente
        val cliente: Clientes = ClientesProvider.getClienteId(idcliente);


        binding.customerDetailTvNameCustomer.text = cliente.name;
        binding.customerDetailTvEmailCustomer.text = cliente.email;
        binding.customerDetailTvPhoneCustomer.text = cliente.phone;
        binding.customerDetailTvCityName.text = cliente.city;
        binding.customerDetailTvAddressCustomer.text = cliente.address;
        binding.customerDetailCiPhoto.setImageResource(cliente.photo);

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.customerDetailFlbtncorrect.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager



            fragmentManager.popBackStack()
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(com.moronlu18.customercreation.R.menu.menu_customer_detail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}