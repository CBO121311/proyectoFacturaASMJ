package com.cbo.customer.ui


import android.os.Bundle
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.cbo.customer.adapter.CustomerAdapter
import com.cbo.customer.usecase.CustomerListViewModel
import com.moronlu18.accounts.entity.Customer
import com.moronlu18.customercreation.R

import com.moronlu18.customercreation.databinding.FragmentCustomerListBinding
import com.moronlu18.invoice.base.BaseFragmentDialog

class CustomerList : Fragment() {

    private var _binding: FragmentCustomerListBinding? = null
    private val binding get() = _binding!!
    private lateinit var customerAdapter: CustomerAdapter
    private var isDeleting = false
    private val viewModel: CustomerListViewModel by viewModels()
    private var isFirstTime = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCustomerListBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)

        binding.viewmodelcustomerlist = this.viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerViewClientes()

        binding.customerListFltbtnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_customerList_to_customerCreation)
        }

        viewModel.getState().observe(viewLifecycleOwner, Observer {
            when (it) {
                is CustomerListState.Loading -> showProgressBar(it.value)
                CustomerListState.NoDataError -> showListEmptyView()
                is CustomerListState.Success -> onSuccess(it.dataset)
                CustomerListState.ReferencedCustomer -> showReferencedCustomer()
                else -> {}
            }
        })

    }

    /**
     * Inicia el recycleview
     */
    private fun initRecyclerViewClientes() {
        customerAdapter = CustomerAdapter(
            onClickListener = { cliente -> onItemSelected(cliente) },
            onClickDelete = { position -> onDeletedItem(position) },
            onClickEdit = { position -> onEditItem(position) })

        binding.customerListRvClientes.layoutManager = LinearLayoutManager(requireContext())
        binding.customerListRvClientes.adapter = customerAdapter
    }

    /**
     * Método que oculta la imagen noData si la lista tiene algo.
     * Y actualiza el adapter.
     */
    private fun onSuccess(dataset: ArrayList<Customer>) {

        binding.customerListClEmpty.visibility = View.GONE
        binding.customerListRvClientes.visibility = View.VISIBLE
        customerAdapter.update(dataset)
    }


    /**
     * Método que realiza la acción de editar un item y comprueba si se puede
     */

    private fun onEditItem(position: Int) {

        val customer = viewModel.getCustomerByPosition(position)
        val possible = viewModel.isDeleteSafe(customer)
        if (!possible) {
            val bundle = Bundle();
            bundle.putInt("position", position)

            parentFragmentManager.setFragmentResult("customkey", bundle)
            findNavController().navigate(R.id.action_customerList_to_customerCreation)
        }
    }

    /**
     * Acción al eliminar un Customer del recycle y comprueba si lo puede hacer
     */
    private fun onDeletedItem(position: Int) {

        isDeleting = true
        val customer = viewModel.getCustomerByPosition(position)
        val possible = viewModel.isDeleteSafe(customer)
        if (!possible) {
            findNavController().navigate(
                CustomerListDirections.actionCustomerListToBaseFragmentDialog2(
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
                    customerAdapter.deleteCustomer(position)
                    viewModel.getCustomerListNoLoading()
                }
            }
    }
}


/**
 * Método muestra el AlertDialog de customer referenciado
 */
private fun showReferencedCustomer() {
    findNavController().navigate(
        CustomerListDirections.actionCustomerListToBaseFragmentDialogWarning(
            getString(R.string.title_ad_warning),
            getString(R.string.errReferencedCustomer)
        )
    )
}


/**
 * Método que muestra la imagen noData si la lista está vacía
 */
private fun showListEmptyView() {
    binding.customerListClEmpty.visibility = View.VISIBLE
    binding.customerListRvClientes.visibility = View.GONE
}


/**
 * Si es true muestra el progressBar, si es false lo quita.
 */
private fun showProgressBar(value: Boolean) {

    if (value) {
        findNavController().navigate(R.id.action_customerList_to_fragmentProgressDialogKiwi)
    } else {
        findNavController().popBackStack()
    }

}

/**
 *  Envía un objeto customer al layout customerDetail utilizando SafeArgs
 */
private fun onItemSelected(customer: Customer) {
    findNavController().navigate(
        CustomerListDirections.actionCustomerListToCustomerDetail(
            customer
        )
    )
}

/**
 * Infla el menú de customerList
 */
override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.menu_customer_list, menu)
}

/**
 * Opciones al seleccionar el menú.
 * Actualmente solo hace el orden de la lista.
 */
override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
        R.id.menu_cd_action_sortname -> {
            customerAdapter.sortName()
            return true
        }

        R.id.menu_cd_action_sortid -> {

            customerAdapter.sortId()
            return true
        }

        else -> return super.onOptionsItemSelected(item)
    }
}

/**
 * Al iniciar el Fragment obtiene la lista con el loading.
 * En la siguientes utiliza una función igual pero sin el loading.
 */
override fun onStart() {
    super.onStart()
    if (isFirstTime) {
        viewModel.getCustomerList()
        isFirstTime = false
    } else {
        viewModel.getCustomerListNoLoading()
    }

}

override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
}

}