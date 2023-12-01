package com.cbo.customer.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.cbo.customer.adapter.CustomerAdapter
import com.cbo.customer.usecase.CustomerViewModel
import com.moronlu18.accounts.entity.Customer
import com.moronlu18.accounts.repository.CustomerProvider
import com.moronlu18.customercreation.R
import com.moronlu18.customercreation.databinding.FragmentCustomerDetailBinding
import com.moronlu18.invoice.base.BaseFragmentDialog

class CustomerDetail : Fragment() {

    private val viewModel: CustomerViewModel by viewModels()
    private val args: CustomerDetailArgs by navArgs()
    private var customerList = CustomerProvider.CustomerdataSet

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
            clientesList = customerList,
        )

        _binding = FragmentCustomerDetailBinding.inflate(inflater, container, false)


        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewnodelcustomerdetail = viewModel

        val custome: Customer = args.customer

        viewModel.let {
            it.nameCustomer.value = custome.name
            it.emailCustomer.value = custome.email.toString()
            it.phoneCustomer.value = custome.phone
            it.cityCustomer.value = custome.city
            it.addressCustomer.value=custome.address
        }

        binding.customerDetailCiPhoto.setImageResource(custome.photo)

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
                deleteConfirmation()
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
    private fun deleteConfirmation() {


        findNavController().navigate(
            CustomerDetailDirections.actionCustomerDetailToBaseFragmentDialog2(
                getString(com.moronlu18.invoice.R.string.title_fragmentDialogExit),
                getString(com.moronlu18.invoice.R.string.Content_fragmentDialogExit)
            )
        )
        //val prueba :Boolean = deleteCusto
       // Log.i("TAG","${prueba}" )
        parentFragmentManager.setFragmentResultListener(
            BaseFragmentDialog.request,
            viewLifecycleOwner
        ) { _, result ->
            val success = result.getBoolean(BaseFragmentDialog.result, false)
            if (success) {
                val position = CustomerProvider.CustomerdataSet.indexOf(args.customer)

                if (position != -1) {
                    CustomerProvider.CustomerdataSet.removeAt(position)
                    adapter.notifyItemRemoved(position)

                    Handler(Looper.getMainLooper()).postDelayed({
                        findNavController().popBackStack()
                    }, 100)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}