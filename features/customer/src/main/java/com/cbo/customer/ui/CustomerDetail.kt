package com.cbo.customer.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.cbo.customer.adapter.CustomerAdapter
import com.moronlu18.accounts.entity.Customer
import com.moronlu18.accounts.repository.CustomerProvider
import com.moronlu18.customercreation.R
import com.moronlu18.customercreation.databinding.FragmentCustomerDetailBinding


class CustomerDetail : Fragment() {

    private val args: CustomerDetailArgs by navArgs()
    private var customerMutableList: MutableList<Customer> = CustomerProvider.dataSet

    private var _binding: FragmentCustomerDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CustomerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        adapter = CustomerAdapter(
            clientesList = customerMutableList,
        )

        _binding = FragmentCustomerDetailBinding.inflate(inflater, container, false)

        val custome: Customer = args.customer
        //val cliente: Customer = CustomerProvider.getCustomerId(idcliente);

        binding.customerDetailTvNameCustomer.text = custome.name;
        binding.customerDetailTvEmailCustomer.text = custome.email;
        binding.customerDetailTvPhoneCustomer.text = custome.phone;
        binding.customerDetailTvCityName.text = custome.city;
        binding.customerDetailTvAddressCustomer.text = custome.address;
        binding.customerDetailCiPhoto.setImageResource(custome.photo);
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.customerDetailFlbtncorrect.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    /**
     * Crea el menú.
     */
    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.menu_customer_detail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     * Opciones al seleccionar el menú.
     */
    @SuppressLint("NotifyDataSetChanged")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.menu_cd_action_delete -> {
                DeleteConfirmation()
                true
            }

            R.id.menu_cd_action_edit -> {
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    /**
     * Confirmación para hacer el borrado.
     */
    private fun DeleteConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirmación")
            .setMessage("¿Estás seguro de que quieres borrar el cliente?")
            .setPositiveButton("Sí") { _, _ ->

                val position = CustomerProvider.dataSet.indexOf(args.customer)

                if (position != -1) {
                    CustomerProvider.dataSet.removeAt(position)
                    adapter.notifyItemRemoved(position)
                    findNavController().popBackStack()
                }
            }
            .setNegativeButton("No", null)
            .show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}