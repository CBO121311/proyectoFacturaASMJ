package com.mto.invoice.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.moronlu18.accounts.entity.Factura
import com.moronlu18.accounts.entity.Item
import com.moronlu18.invoicelist.databinding.FragmentInvoiceDetailBinding
import com.mto.invoice.adapter.ItemAdapter
import com.mto.invoice.usecase.InvoiceDetailViewModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class InvoiceDetail : Fragment(){

    private val args: InvoiceDetailArgs by navArgs()
    private var _binding: FragmentInvoiceDetailBinding? = null
    private val viewmodel: InvoiceDetailViewModel by viewModels()

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentInvoiceDetailBinding.inflate(inflater, container, false)
        val formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault())
        val invoice: Factura = args.invoice
        binding.invoiceDetailTcCliText.text = viewmodel.giveNom(invoice.customerId)
        binding.invoiceDetailTcFechaEmText.text = formatoFecha.format(invoice.issuedDate)
        binding.invoiceDetailTcFechaFText.text = formatoFecha.format(invoice.dueDate)
        binding.invoiceDetailTvTotal.text = viewmodel.giveTotal(invoice.lineItems!!.toMutableList())
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





}