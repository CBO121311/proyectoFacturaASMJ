package com.cbo.customerdetail.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.moronlu18.customerdetail.databinding.FragmentCustomerDetailBinding

class CustomerDetail : Fragment() {

    //private val args: CustomerDetailArgs by navArgs()
    //private val idCliente = args.customerPrueba
    //private val args: CustomerListArgs by NavArgs()
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

        inflater.inflate(com.moronlu18.customerdetail.R.menu.menu_customer_detail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}


/**Pruebas futuras
 *    override fun onCreateView(
 *         inflater: LayoutInflater, container: ViewGroup?,
 *         savedInstanceState: Bundle?
 *     ): View? {
 *         //val args: FragmentCustomerDetailBinding by navArgs<>()
 *         // Inflate the layout for this fragment
 *
 *         /*binding.customerDetailTvNameCustomer.text = cliente.name;
 *         binding.customerDetailTvEmailCustomer.text = cliente.email;
 *         binding.customerDetailTvPhoneCustomer.text = cliente.phone;
 *         binding.customerDetailTvCity.text = cliente.city;
 *         binding.customerDetailTvAddressCustomer.text = cliente.address;*/
 *
 *         _binding = FragmentCustomerDetailBinding.inflate(inflater, container, false)
 *
 *         //binding.customerDetailTvAddressCustomer.text = idCliente
 *         return binding.root
 *     }
 */