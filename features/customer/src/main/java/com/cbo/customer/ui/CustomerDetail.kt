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
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.cbo.customer.usecase.CustomerDetailViewModel
import com.moronlu18.accounts.entity.Customer
import com.moronlu18.customercreation.R
import com.moronlu18.customercreation.databinding.FragmentCustomerDetailBinding
import com.moronlu18.invoice.base.BaseFragmentDialog

class CustomerDetail : Fragment() {

    private val viewModel: CustomerDetailViewModel by viewModels()
    private val args: CustomerDetailArgs by navArgs()
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

        binding.viewnodelcustomerdetail = this.viewModel
        binding.lifecycleOwner = this



        viewModel.getState().observe(viewLifecycleOwner, Observer {
            when (it) {
                CustomerDetailState.OnSuccess -> onSuccess()
                CustomerDetailState.ReferencedCustomer -> showReferencedCustomer()
            }
        })

        return binding.root
    }

    private fun onSuccess() {
        val custome: Customer = args.customer

        viewModel.let {
            it.idCustomer.value = custome.id.toString()
            it.nameCustomer.value = custome.name
            it.emailCustomer.value = custome.email.toString()
            it.phoneCustomer.value = custome.phone
            it.cityCustomer.value = custome.city
            it.addressCustomer.value = custome.address
        }
        binding.customerDetailCiPhoto.setImageResource(custome.photo)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.customerDetailFlbtncorrect.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    /**
     * Acción al eliminar un Customer del repositorio
     * y comprueba si lo puede hacer
     */
    private fun deleteConfirmation() {
        val possible = viewModel.isDeleteSafe(args.customer)
        if (!possible) {

            findNavController().navigate(
                CustomerDetailDirections.actionCustomerDetailToBaseFragmentDialog2(
                    getString(R.string.title_deleteCustomer),
                    getString(R.string.Content_deleteCustomer)
                )
            )
            parentFragmentManager.setFragmentResultListener(
                BaseFragmentDialog.request,
                viewLifecycleOwner
            ) { _, result ->
                val success = result.getBoolean(BaseFragmentDialog.result, false)
                if (success) {
                    viewModel.deleteCustomer(args.customer)

                    Handler(Looper.getMainLooper()).postDelayed({
                        findNavController().popBackStack()
                    }, 100)
                }
            }
        }
    }


    /**
     * Infla el menú
     */
    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.menu_customer_detail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     * Opciones al seleccionar el menú.
     * Solo se ha dado al funcionalidad al borrado.
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
     * Método muestra el AlertDialog de customer referenciado
     */
    private fun showReferencedCustomer() {
        findNavController().navigate(
            CustomerDetailDirections.actionCustomerDetailToBaseFragmentDialogWarning(
                getString(R.string.title_ad_warning),
                getString(R.string.errReferencedCustomer)
            )
        )
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        viewModel.onSuccess()
    }
}