package com.mto.invoice.ui

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.moronlu18.accounts.entity.Item
import com.moronlu18.data.invoice.Invoice
import com.moronlu18.data.invoice.InvoiceStatus
import com.moronlu18.invoice.base.BaseFragmentDialog
import com.moronlu18.invoice.ui.MainActivity
import com.moronlu18.invoicelist.R
import com.moronlu18.invoicelist.databinding.FragmentInvoiceDetailBinding
import com.mto.invoice.adapter.detail.ItemAdapter
import com.mto.invoice.usecase.InvoiceDetailState
import com.mto.invoice.usecase.InvoiceDetailViewModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class InvoiceDetail : Fragment(), MenuProvider {

    private var _binding: FragmentInvoiceDetailBinding? = null
    private val viewmodeldetail: InvoiceDetailViewModel by viewModels()
    private val doubleClickDelay = 200L
    private lateinit var invoice: Invoice
    private var idInvoice: Int = 0

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInvoiceDetailBinding.inflate(inflater, container, false)
        binding.viewmodel = this.viewmodeldetail
        binding.lifecycleOwner = this
        return binding.root;

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpFab()
        setUpToolbar()

        viewmodeldetail.getState().observe(viewLifecycleOwner, Observer {
            when (it) {
                InvoiceDetailState.OnSuccess -> onSuccess()
                else -> {}
            }
        })
        parentFragmentManager.setFragmentResultListener("detailkey", this,
            FragmentResultListener { _, result ->
                idInvoice = result.getInt("detailposition")
                invoice = viewmodeldetail.getInvoiceById(idInvoice)
            }
        )
    }
    override fun onStart() {
        super.onStart()
        viewmodeldetail.onSuccess()
    }

    override fun onResume() {
        super.onResume()
        invoice = viewmodeldetail.getInvoiceById(idInvoice)
        viewmodeldetail.onSuccess()
    }

    private fun onSuccess() {
        val formatoFecha =
            DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault())
        val customer = viewmodeldetail.getCustomerById(invoice.customerId)
        val listaItem = viewmodeldetail.getListItemByInvoiceId(invoice.id)
        viewmodeldetail.let {
            it.user.value = customer?.name
            it.startDate.value = formatoFecha.format(invoice.issuedDate)
            it.endDate.value = formatoFecha.format(invoice.dueDate)
            it.status.value = giveStatusText(invoice.status)
            it.number.value = invoice.number
            it.total.value = viewmodeldetail.giveTotal(listaItem)
            binding.invoiceDetailTvEstado.setTextColor(setColorEstado(invoice.status.toString()))
            initReciclerView(listaItem as MutableList<Item>)
        }

    }

    /**
     * Función que devuelve un entero que representa un color,
     * en función del estado de la factura
     */
    fun setColorEstado(status: String): Int {
        return if (status.equals("PENDIENTE")) {
            Color.parseColor("#FF1100")
        }else if(status.equals("PAGADA")) {
            Color.parseColor("#217C00")
        }else {
            Color.parseColor("#978303")
        }

    }

    /**
     * Funcion que devuelve un texto en funcin del tipo de factura
     */
    fun giveStatusText(tipo: InvoiceStatus): String {
        return if(tipo == InvoiceStatus.PENDIENTE) {
            getString(R.string.invoiceStatusPendiente)
        }else if(tipo == InvoiceStatus.PAGADA) {
            getString(R.string.invoiceStatusPagada)
        }else {
            getString(R.string.invoiceStatusVencida)
        }

    }

    private fun initReciclerView(lista: MutableList<Item>) {
        val manager = LinearLayoutManager(requireContext())

        binding.invoiceDetailRvArticulos.layoutManager = manager
        binding.invoiceDetailRvArticulos.adapter =
            ItemAdapter(lista) { item ->
                onItemSelected(
                    item
                )
            }
    }

    fun onItemSelected(item: Item) {
        Toast.makeText(requireContext(), item.name, Toast.LENGTH_SHORT).show()
    }

    private fun setUpFab() {
        (requireActivity() as? MainActivity)?.fab?.apply {
            visibility = View.VISIBLE
            setImageResource(R.drawable.ic_action_correct)
            setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun setUpToolbar() {
        (requireActivity() as? MainActivity)?.toolbar?.apply {
            visibility = View.VISIBLE
        }

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_invoice_detail, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.menu_cd_action_delete -> {
                Handler(Looper.getMainLooper()).postDelayed({
                    onDeleteItem()
                }, doubleClickDelay)
                true
            }

            R.id.menu_cd_action_edit -> {
                Handler(Looper.getMainLooper()).postDelayed({
                    onEditItem(invoice)
                }, doubleClickDelay)
                true
            }

            else -> false
        }
    }
    /**
     * Función que navega a la interfaz de creación para editar los datos de la factura
     */
    private fun onEditItem(invoice: Invoice) {
        val idInvoice = invoice.id.value
        val bundle = Bundle();
        bundle.putInt("position", idInvoice)
        parentFragmentManager.setFragmentResult("invoicekey", bundle)
        findNavController().navigate(R.id.action_invoiceDetail_to_invoiceCreation)

    }

    /**
     * Función que borra una factura y pide confirmación
     */
    private fun onDeleteItem() {
        findNavController().navigate(
            InvoiceDetailDirections.actionInvoiceDetailToBaseFragmentDialog2(
                getString(R.string.title_deleteInvoice),
                getString(R.string.Content_deleteInvoice)
            )
        )
        parentFragmentManager.setFragmentResultListener(
            BaseFragmentDialog.request,
            viewLifecycleOwner
        ) { _, result ->
            val success = result.getBoolean(BaseFragmentDialog.result, false)
            if (success) {
                //ToDo error por clave foranea
                viewmodeldetail.delete(invoice)
                Handler(Looper.getMainLooper()).postDelayed({
                    findNavController().popBackStack()
                }, 100)
            }
        }

    }

    /**
     * Función que libera el binding
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}