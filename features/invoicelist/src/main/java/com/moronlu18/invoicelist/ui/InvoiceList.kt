package com.moronlu18.invoicelist.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.moronlu18.invoicelist.adapter.FacturaAdapter
import com.moronlu18.invoicelist.data.Factura
import com.moronlu18.invoicelist.data.FacturaProvider
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fab = requireActivity().findViewById<FloatingActionButton>(com.moronlu18.invoice.R.id.fab)
        fab.visibility = View.GONE

        initRecyclerView()
    }

    fun initRecyclerView() {
        val manager = LinearLayoutManager(context)
        val decoration = DividerItemDecoration(context, manager.orientation)

        binding.invoiceListRvFacturas.layoutManager = LinearLayoutManager(context)
        binding.invoiceListRvFacturas.adapter = FacturaAdapter(FacturaProvider.facturaList) {
            onItemSelected(
                it
            )
        }
        binding.invoiceListRvFacturas.addItemDecoration(decoration)
        binding.tvVacio.text="";

    }

    fun onItemSelected(factura: Factura) {
        binding.invoiceListRvFacturas.setOnClickListener {
            Toast.makeText(requireContext(),factura.id, Toast.LENGTH_SHORT).show()

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}