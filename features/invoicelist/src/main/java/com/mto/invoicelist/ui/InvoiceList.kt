package com.mto.invoicelist.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mto.invoicelist.adapter.FacturaAdapter
import com.mto.invoicelist.data.Factura
import com.mto.invoicelist.data.FacturaProvider
import com.moronlu18.invoicelist.databinding.FragmentInvoiceListBinding


class InvoiceList : Fragment() {

    private var _binding : FragmentInvoiceListBinding? = null

    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentInvoiceListBinding.inflate(inflater,container,false)
        binding.fab.setOnClickListener {
            findNavController().navigate(com.moronlu18.invoice.R.id.action_InvoiceListFragment_to_InvoiceCreationFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
    }

    fun initRecyclerView() {
        val manager = LinearLayoutManager(context)

        binding.invoiceListRvFacturas.layoutManager = LinearLayoutManager(context)
        binding.invoiceListRvFacturas.adapter = FacturaAdapter(FacturaProvider.facturaList) {
            onItemSelected(
                it
            )
        }
        binding.tvVacio.text="";


    }

    fun onItemSelected(factura: Factura) {
        findNavController().navigate(com.moronlu18.invoice.R.id.action_InvoiceListFragment_to_InvoiceDetailFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}