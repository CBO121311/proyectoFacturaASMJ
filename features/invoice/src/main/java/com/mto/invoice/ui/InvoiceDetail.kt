package com.mto.invoice.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.moronlu18.accounts.entity.Factura
import com.moronlu18.accounts.entity.Item
import com.moronlu18.accounts.entity.Task
import com.moronlu18.accounts.repository.CustomerProvider
import com.moronlu18.accounts.repository.ItemProvider
import com.moronlu18.invoicelist.databinding.FragmentInvoiceDetailBinding
import com.mto.invoice.adapter.ItemAdapter
import java.time.Instant


class InvoiceDetail : Fragment() {

    private val args: InvoiceDetailArgs by navArgs()
    private var _binding: FragmentInvoiceDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentInvoiceDetailBinding.inflate(inflater, container, false)

        val invoice: Factura = args.invoice
        binding.invoiceDetailTcCliText.text = CustomerProvider.getNom(invoice.customerId)
        binding.invoiceDetailTcFechaEmText.text = invoice.issuedDate
        binding.invoiceDetailTcFechaFText.text = invoice.dueDate
        binding.invoiceDetailTvTotal.text = ItemProvider.getTotal(invoice.lineItems!!.toMutableList())
        binding.invoiceDetailTvEstado.text = invoice.status.toString()
        return binding.root;

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initReciclerView()

        binding.invoiceDetailFabCheck.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun initReciclerView() {
        val manager = LinearLayoutManager(requireContext())

        binding.invoiceDetailRvArticulos.layoutManager = manager
        binding.invoiceDetailRvArticulos.adapter = ItemAdapter(args.invoice.lineItems!!.toMutableList()) { item ->
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
    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(com.moronlu18.invoicelist.R.menu.menu_invoice_detail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

}