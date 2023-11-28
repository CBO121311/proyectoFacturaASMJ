package com.mto.invoice.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.moronlu18.accounts.entity.Customer
import com.moronlu18.invoicelist.R
import com.mto.invoice.adapter.FacturaAdapter
import com.moronlu18.accounts.entity.Factura
import com.moronlu18.accounts.entity.Item
import com.moronlu18.accounts.repository.CustomerProvider
import com.moronlu18.accounts.repository.FacturaProvider
import com.moronlu18.invoicelist.databinding.FragmentInvoiceListBinding


class InvoiceList : Fragment() {

    private var _binding : FragmentInvoiceListBinding? = null

    private val binding get() = _binding!!

    private var FacturaMutableList:MutableList<Factura> = FacturaProvider.dataSet
    private lateinit var adapter: FacturaAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentInvoiceListBinding.inflate(inflater,container,false)
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_invoiceList_to_invoiceCreation)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
    }

    fun initRecyclerView() {
        adapter = FacturaAdapter(
            facturaList =  FacturaMutableList,
            onClickListener = {Factura -> onItemSelected(Factura)},
            onClickDelete = {position -> onDeleteItem(position)}
        )
        updateEmptyView()
        val manager = LinearLayoutManager(context)
        binding.invoiceListRvFacturas.layoutManager = manager
        binding.invoiceListRvFacturas.adapter = adapter


    }
    private fun onDeleteItem(position: Int) {
        FacturaMutableList.removeAt(position)
        adapter.notifyItemRemoved(position)
        updateEmptyView()
    }
    private fun onItemSelected(invoice: Factura) {
        findNavController().navigate(InvoiceListDirections.actionInvoiceListToInvoiceDetail(invoice))

    }
    private fun updateEmptyView() {

        if (FacturaProvider.dataSet.isEmpty()) {
            binding.invoiceCreationLayoutVacio.visibility = View.VISIBLE
        } else {
            binding.invoiceCreationLayoutVacio.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}