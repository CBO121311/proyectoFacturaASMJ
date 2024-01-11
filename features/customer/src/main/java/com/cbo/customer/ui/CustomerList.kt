package com.cbo.customer.ui


import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
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
    private val doubleClickDelay = 200L

    //prevenir doble click
    private var mLastClickTime: Long = 0

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
        setUpFab()
        initRecyclerViewCustomer()

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
     * Inicializa y configura el RecyclerView para mostrar la lista de clientes.
     */
    private fun initRecyclerViewCustomer() {
        customerAdapter = CustomerAdapter(this)
        binding.customerListRvClientes.layoutManager = LinearLayoutManager(requireContext())
        binding.customerListRvClientes.adapter = customerAdapter
    }

    /**
     * Se ejecuta al hacer clic en un elemento de la lista.
     */
    override fun customerClick(position: Int) {
        navigateDetailCustomer(position)
    }

    /**
     * Se ejecuta al mantener pulsado un elemento de la lista.
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun customerOnLongClick(view: View, position: Int, customer: Customer) {
        showPopUpMenu(view, position, customer)
    }

    /**
     * Se le llama en caso de éxito.
     * Acción cuando se obtiene con éxito la lista de clientes.
     * Actualiza la interfaz de usuario para mostrar la lista de clientes.
     */
    private fun onSuccess(dataset: ArrayList<Customer>) {

        binding.customerListClEmpty.visibility = View.GONE
        binding.customerListRvClientes.visibility = View.VISIBLE
        customerAdapter.update(dataset)
    }


    /**
     * Navega a la pantalla de detalle del cliente cuando se hace clic en un cliente de la lista.
     */
    private fun navigateDetailCustomer(position: Int) {

        val bundle = Bundle()
        bundle.putInt("detailposition", position)
        parentFragmentManager.setFragmentResult("detailkey", bundle)

        findNavController().navigate(R.id.action_customerList_to_customerDetail)
    }


    /**
     * Envía la posición de la lista de cliente al que se desea editar.
     */
    private fun onEditItem(position: Int) {

        val bundle = Bundle();
        bundle.putInt("customposition", position)

        parentFragmentManager.setFragmentResult("customkey", bundle)
        findNavController().navigate(R.id.action_customerList_to_customerCreation)
    }

    /**
     * Comprueba si es seguro realizar la eliminación del cliente.
     * Muestra un alertdialog de confirmación si es capaz de eliminarlo.
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
     * Configura el toolbar para que sea visible.
     */
    private fun setUpToolbar() {
        (requireActivity() as? MainActivity)?.toolbar?.apply {
            visibility = View.VISIBLE
        }

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


    /**
     * Muestra un menu popup al hacer pulsación larga de la lista de clientes.
     */

    @RequiresApi(Build.VERSION_CODES.Q)
    fun showPopUpMenu(view: View, position: Int, customer: Customer) {
        val popupMenu = PopupMenu(requireContext(), view, Gravity.END)

        popupMenu.inflate(R.menu.menu_pop_item)

        popupMenu.setOnMenuItemClickListener {

            //SystemClock.elapsedRealtime() obtener el tiempo actual en milisegundos
            if (SystemClock.elapsedRealtime() - mLastClickTime < doubleClickDelay) {
                return@setOnMenuItemClickListener true;
            }
            mLastClickTime = SystemClock.elapsedRealtime();

            when (it.itemId) {
                R.id.menupop_see -> {
                    navigateDetailCustomer(position)
                    true
                }

                R.id.menupop_edit -> {
                    onEditItem(position)
                    true
                }

                R.id.menupop_remove -> {
                    onDeletedItem(customer)
                    true
                }

                else -> false
            }
        }
        popupMenu.setOnDismissListener {
            customerAdapter.clearSelection()
        }

        popupMenu.setForceShowIcon(true)
        popupMenu.show()
    }


    /**
     * Infla el menú de la lista de clientes.
     */
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_customer_list, menu)
    }


    /**
     * Se llama cuando se selecciona se selecciona una de las opciones del toolbar.
     * Actualmente ordena la lista y refresca la lista.
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
     * Configura el botón flotante para que sea visible y se le añade un ícono.
     * Al pulsarlo navega a la creación de cliente.
     */
    private fun setUpFab() {
        (requireActivity() as? MainActivity)?.fab?.apply {
            visibility = View.VISIBLE
            setImageResource(R.drawable.ic_action_add)
            setOnClickListener {
                findNavController().navigate(R.id.action_customerList_to_customerCreation)
            }
        }
    }


    /**
     * Muestra alertDialog de advertencia cuando se intenta eliminar un cliente referenciado.
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
     * Muestra la vista de lista vacía y oculta el RecyclerView.
     */
    private fun showListEmptyView() {
        binding.customerListClEmpty.visibility = View.VISIBLE
        binding.customerListRvClientes.visibility = View.GONE
    }


    /**
     * Muestra o quita el ProgressBar según el valor proporcionado.
     */
    private fun showProgressBar(value: Boolean) {

        if (value) {
            findNavController().navigate(R.id.action_customerList_to_fragmentProgressDialogKiwi)
        } else {
            findNavController().popBackStack()
        }
    }


    /**
     * Se llama al iniciar el Fragment. Obtiene la lista con el loading la primera vez.
     * En las siguientes llamadas utiliza una función pero sin el loading.
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

    /**
     * Liberamos la referencia al binding.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}