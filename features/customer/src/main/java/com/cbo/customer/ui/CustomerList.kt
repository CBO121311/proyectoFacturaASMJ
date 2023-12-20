package com.cbo.customer.ui


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cbo.customer.adapter.CustomerAdapter
import com.cbo.customer.usecase.CustomerListViewModel
import com.moronlu18.accounts.entity.Customer
import com.moronlu18.customercreation.R

import com.moronlu18.customercreation.databinding.FragmentCustomerListBinding
import com.moronlu18.invoice.base.BaseFragmentDialog
import com.moronlu18.invoice.ui.MainActivity

class CustomerList : Fragment(), MenuProvider, CustomerAdapter.OnCustomerClick {

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

        binding.viewmodelcustomerlist = this.viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        initRecyclerViewCustomer()

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
    private fun initRecyclerViewCustomer() {
        customerAdapter = CustomerAdapter(this)
        binding.customerListRvClientes.layoutManager = LinearLayoutManager(requireContext())
        binding.customerListRvClientes.adapter = customerAdapter
    }

    /**
     *  Envía un objeto customer al layout customerDetail utilizando SafeArgs
     */
    override fun customerClick(customer: Customer) {
        findNavController().navigate(
            CustomerListDirections.actionCustomerListToCustomerDetail(
                customer
            )
        )
    }

    override fun customerOnLongClick(customer: Customer) {
        //showToolbarMenu()
        onDeletedItem(customer)
    }

    override fun customerEditClick(position: Int) {
        onEditItem(position)
    }



    private fun setUpToolbar() {
        (requireActivity() as? MainActivity)?.toolbar?.apply {
            visibility = View.VISIBLE
        }

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    /**
     *  Esta función personaliza el comportamiento de la Toolbar de la Activity
     */

    @SuppressLint("ClickableViewAccessibility")
    private fun showToolbarMenu() {
        (requireActivity() as? MainActivity)?.toolbar?.apply {
            visibility = View.VISIBLE
        }

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        // Configura un listener de clic en otro lugar para ocultar el Toolbar
        val rootView = (activity as? MainActivity)?.findViewById<View>(android.R.id.content)
        rootView?.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                hideToolbarMenu()
            }
            false
        }
    }

    /**
     * Oculta los botones del menú en el Toolbar
     */
    private fun hideToolbarMenu() {
        (requireActivity() as? MainActivity)?.toolbar?.apply {
            visibility = View.VISIBLE
        }

        val menuHost: MenuHost = requireActivity()
        menuHost.removeMenuProvider(this)
    }

    /**
     * Método que realiza la acción de editar un item y comprueba si se puede
     */

    private fun onEditItem(position: Int) {

        //val customer = viewModel.getCustomerByPosition(position)
        //if (viewModel.isDeleteSafe(customer)) {
            val bundle = Bundle();
            bundle.putInt("position", position)

            parentFragmentManager.setFragmentResult("customkey", bundle)
            findNavController().navigate(R.id.action_customerList_to_customerCreation)
        //}
    }

    /**
     * Acción al eliminar un Customer del recycle y comprueba si lo puede hacer
     */
    private fun onDeletedItem(customer: Customer) {

        isDeleting = true
        if (viewModel.isDeleteSafe(customer)) {
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
                    customerAdapter.deleteCustomer(customer)
                    viewModel.getCustomerListNoLoading()
                }
            }
        }
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
     * Función que muestra el AlertDialog del estado ReferencedCustomer
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
     * Infla el menú de customerList
     */
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_customer_list, menu)
    }


    /**
     * Opciones al seleccionar el menú.
     * Actualmente solo hace el orden de la lista.
     */
    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {

            R.id.menu_cd_action_refresh -> {
                viewModel.getCustomerList()
                true
            }

            R.id.menu_cd_action_sortname -> {
                customerAdapter.sortName()
                true
            }

            else -> false
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