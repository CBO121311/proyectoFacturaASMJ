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
import com.moronlu18.accounts.entity.Factura
import com.moronlu18.accounts.entity.Item
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
    private lateinit var factura: Factura
    private var posInvoice: Int = 0

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
            }
        })
        parentFragmentManager.setFragmentResultListener("detailkey", this,
            FragmentResultListener { _, result ->
                posInvoice = result.getInt("detailposition")
                factura = viewmodeldetail.getInvoiceByPos(posInvoice)
            }
        )


    }

    private fun onSuccess() {
        val formatoFecha =
            DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault())

        viewmodeldetail.let {
            it.user.value = factura.customer.name
            it.startDate.value = formatoFecha.format(factura.issuedDate)
            it.endDate.value = formatoFecha.format(factura.dueDate)
            it.status.value = factura.status.toString()
            it.total.value = viewmodeldetail.giveTotal(factura.lineItems!!.toMutableList())
            binding.invoiceDetailTvEstado.setTextColor(setColorEstado(factura.status.toString()))
            initReciclerView()
        }

    }

    /**
     * Función que devuelve un entero que representa un color,
     * en función del estado de la factura
     */
    fun setColorEstado(status: String): Int {
        if (status.equals("Pendiente")) {
            return Color.parseColor("#FF1100")
        }else if(status.equals("Pagada")) {
            return Color.parseColor("#217C00")
        }else {
            return Color.parseColor("#978303")
        }

    }


    private fun initReciclerView() {
        val manager = LinearLayoutManager(requireContext())

        binding.invoiceDetailRvArticulos.layoutManager = manager
        binding.invoiceDetailRvArticulos.adapter =
            ItemAdapter(factura.lineItems!!.toMutableList()) { item ->
                onItemSelected(
                    item
                )
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
                    onEditItem(factura)
                }, doubleClickDelay)
                true
            }

            else -> false
        }
    }
    override fun onStart() {
        super.onStart()
        viewmodeldetail.onSuccess()
    }

    override fun onResume() {
        super.onResume()

        factura = viewmodeldetail.getInvoiceByPos(posInvoice)
        viewmodeldetail.onSuccess()
    }

    private fun onEditItem(factura: Factura) {
        val posInvoice = viewmodeldetail.getPosByInvoice(factura)
        val bundle = Bundle();
        bundle.putInt("position", posInvoice)
        parentFragmentManager.setFragmentResult("invoicekey", bundle)
        findNavController().navigate(R.id.action_invoiceDetail_to_invoiceCreation)

    }

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
                viewmodeldetail.deleteInvoice(factura)
                Handler(Looper.getMainLooper()).postDelayed({
                    findNavController().popBackStack()
                }, 100)
            }
        }

    }

}