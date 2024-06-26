package com.cbo.customer.ui


import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.cbo.customer.usecase.CustomerDetailViewModel
import com.google.android.material.snackbar.Snackbar
import com.moronlu18.data.customer.Customer
import com.moronlu18.customercreation.R
import com.moronlu18.customercreation.databinding.FragmentCustomerDetailBinding
import com.moronlu18.invoice.base.BaseFragmentDialog
import com.moronlu18.invoice.base.BaseFragmentDialogWarning
import com.moronlu18.invoice.ui.MainActivity

class CustomerDetail : Fragment(), MenuProvider {

    private val viewModel: CustomerDetailViewModel by viewModels()
    private var _binding: FragmentCustomerDetailBinding? = null
    private val binding get() = _binding!!
    private val args: CustomerDetailArgs by navArgs()
    private val doubleClickDelay = 200L
    private var mLastClickTime: Long = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCustomerDetailBinding.inflate(inflater, container, false)

        binding.viewnodelcustomerdetail = this.viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpFab()
        setUpToolbar()

        viewModel.getState().observe(viewLifecycleOwner, Observer {
            when (it) {
                CustomerDetailState.OnSuccess -> onSuccess()
                CustomerDetailState.ReferencedCustomer -> showReferencedCustomer()
            }
        })
    }


    /**
     * Se le llama en caso de éxito.
     * Utiliza el databinding para insertar los datos.
     */
    private fun onSuccess() {

        val customer = args.customnav
        viewModel.let {
            it.idCustomer.value = customer.id.value.toString()
            it.nameCustomer.value = customer.name
            it.emailCustomer.value = customer.email.toString()
            it.phoneCustomer.value = isValue(customer.phone)
            it.cityCustomer.value = isValue(customer.city)
            it.addressCustomer.value = isValue(customer.address)
        }
        when {
            customer.photo != null -> binding.customerDetailCiPhoto.setImageURI(customer.photo)
            else -> binding.customerDetailCiPhoto.setImageResource(R.drawable.kiwidinero)

        }
    }


    /**
     * Comprueba si se puede eliminar un cliente o está referenciado.
     * Y si es posible, muestra un alertDialog de confirmación antes de eliminar el cliente.
     */

    private fun deleteConfirmation() {

        val customer = args.customnav

        when {
            !viewModel.isCustomerReferenced(customer) -> {
                val dialog = BaseFragmentDialog.newInstance(
                    getString(R.string.title_deleteCustomer),
                    getString(R.string.Content_deleteCustomer)
                )
                dialog.show(childFragmentManager,"delete_dialog")

                dialog.parentFragmentManager.setFragmentResultListener(
                    BaseFragmentDialog.request,
                    viewLifecycleOwner
                ) { _, result ->

                    when {
                        result.getBoolean(BaseFragmentDialog.result) -> {
                            viewModel.delete(customer)
                            Snackbar.make(requireView(), getString(R.string.customer_snackbar_delete_customer), Snackbar.LENGTH_LONG).show()
                            findNavController().popBackStack()
                        }
                    }
                }
            }
        }
    }

    /**
     * Configura toolbar para la pantalla de detalle del cliente.
     */
    private fun setUpToolbar() {
        (requireActivity() as? MainActivity)?.toolbar?.apply {
            visibility = View.VISIBLE
        }

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    /**
     * Infla el menú de opciones para la vista de lista de clientes.
     */
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_customer_detail, menu)
    }

    /**
     * Invocado cuando se selecciona un elemento del menú de opciones.
     */
    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        val customer = args.customnav
        //Evitar la doble pulsación.
        when {
            SystemClock.elapsedRealtime() - mLastClickTime < doubleClickDelay -> return true;
            else -> {
                mLastClickTime = SystemClock.elapsedRealtime();

                return when (menuItem.itemId) {

                    R.id.menu_cd_action_delete -> {
                        deleteConfirmation()
                        true
                    }

                    R.id.menu_cd_action_edit -> {
                        onEditItem(customer)
                        true
                    }

                    else -> false
                }
            }
        }
    }


    /**
     * Devuelve valor predeterminado ("N/a") si el valor dado es nulo o vacío.
     */
    private fun isValue(value: String?): String {

        return when {
            value.isNullOrBlank() -> getString(R.string.cd_desc)
            else -> value
        }
    }

    /**
     * Función que muestra el AlertDialog si el estado es ReferencedCustomer.
     */
    private fun showReferencedCustomer() {

        val dialog = BaseFragmentDialogWarning.newInstance(
            getString(R.string.title_ad_warning),
            getString(R.string.errReferencedCustomer)
        )
        dialog.show(childFragmentManager,"warning_dialog")
    }

    /**
     * Configura el botón flotante para la pantalla de detalle del cliente y se le asigna un icono
     * Vuelve a la pantalla anterior al hacer clic.
     */
    private fun setUpFab() {
        (requireActivity() as? MainActivity)?.fab?.apply {

            visibility = View.VISIBLE
            setImageResource(R.drawable.ic_action_check)

            setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    /**
     * Inicia la edición de un cliente cuando se selecciona la opción de edición.
     */
    private fun onEditItem(customer: Customer) {

        findNavController().navigate(
            CustomerDetailDirections.actionCustomerDetailToCustomerCreation(
                customer
            )
        )
    }

    /**
     * Invoca el método "onSuccess" del ViewModel cuando inicia el fragmento
     */
    override fun onStart() {
        super.onStart()
        viewModel.onSuccess()
    }


    /**
     * Liberamos la referencia al binding.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}