package com.cbo.customer.ui


import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.cbo.customer.usecase.CustomerDetailViewModel
import com.moronlu18.data.customer.Customer
import com.moronlu18.customercreation.R
import com.moronlu18.customercreation.databinding.FragmentCustomerDetailBinding
import com.moronlu18.invoice.base.BaseFragmentDialog
import com.moronlu18.invoice.ui.MainActivity

class CustomerDetail : Fragment(), MenuProvider {

    private val viewModel: CustomerDetailViewModel by viewModels()
    private var _binding: FragmentCustomerDetailBinding? = null
    private val binding get() = _binding!!
    private var customer: Customer? = null
    private var posCostumer: Int = 0
    private val doubleClickDelay = 200L

    //prevenir doble click
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

        parentFragmentManager.setFragmentResultListener("detailkey", this,
            FragmentResultListener { _, result ->
                posCostumer = result.getInt("detailposition")
                customer = viewModel.getCustomerByPosition(posCostumer)
            }
        )
    }


    /**
     * Se le llama en caso de éxito.
     * Utiliza el databinding para insertar los datos.
     */
    private fun onSuccess() {

        viewModel.let {
            it.idCustomer.value = customer?.id?.value.toString()
            it.nameCustomer.value = customer?.name
            it.emailCustomer.value = customer?.email.toString()
            it.phoneCustomer.value = isValue(customer?.phone)
            it.cityCustomer.value = isValue(customer?.city)
            it.addressCustomer.value = isValue(customer?.address)
        }
        if (customer?.phototrial != null) {
            binding.customerDetailCiPhoto.setImageResource(customer?.phototrial!!)
        } else {
            binding.customerDetailCiPhoto.setImageBitmap(customer?.photo!!)
        }
    }


    /**
     * Comprueba si se puede eliminar un cliente o está referenciado.
     * Y si es posible, muestra un alertDialog de confirmación antes de eliminar el cliente.
     */
    private fun deleteConfirmation() {

        if (viewModel.isDeleteSafe(customer!!)) {

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
                    viewModel.deleteCustomer(customer!!)

                    Handler(Looper.getMainLooper()).postDelayed({
                        findNavController().popBackStack()
                    }, 100)
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

        //Evitar la doble pulsación.
        if (SystemClock.elapsedRealtime() - mLastClickTime < doubleClickDelay) {
            return true;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        return when (menuItem.itemId) {

            R.id.menu_cd_action_delete -> {
                deleteConfirmation()
                true
            }

            R.id.menu_cd_action_edit -> {
                onEditItem(customer!!)
                true
            }

            else -> false
        }
    }


    /**
     * Devuelve valor predeterminado ("N/a") si el valor dado es nulo o vacío.
     */
    private fun isValue(value: String?): String {

        return if (value.isNullOrBlank()) {
            "N/a"
        } else {
            value
        }
    }

    /**
     * Función que muestra el AlertDialog si el estado es ReferencedCustomer.
     */
    private fun showReferencedCustomer() {
        findNavController().navigate(
            CustomerDetailDirections.actionCustomerDetailToBaseFragmentDialogWarning(
                getString(R.string.title_ad_warning),
                getString(R.string.errReferencedCustomer)
            )
        )
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

        val posCustomer = viewModel.getPositionByCustomer(customer)

        val bundle = Bundle();
        bundle.putInt("customposition", posCustomer)

        parentFragmentManager.setFragmentResult("customkey", bundle)
        findNavController().navigate(R.id.action_customerDetail_to_customerCreation)
    }

    /**
     * Invoca el método "onSuccess" del ViewModel cuando inicia el fragmento
     */
    override fun onStart() {
        super.onStart()

        //Todo Remedio temporal hasta que se solucione idioma
        customer = viewModel.getCustomerByPosition(posCostumer)
        viewModel.onSuccess()

    }

    /**
     * Actualiza el cliente obtenido por la posición y llama a "onSuccess"
     */
    override fun onResume() {
        super.onResume()
            customer = viewModel.getCustomerByPosition(posCostumer)
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