package com.mto.invoice.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.moronlu18.invoicelist.R
import com.mto.invoice.adapter.FacturaAdapter
import com.moronlu18.accounts.entity.Factura
import com.moronlu18.invoicelist.databinding.FragmentInvoiceListBinding
import com.mto.invoice.usecase.InvoiceListState
import com.mto.invoice.usecase.InvoiceListViewModel


class InvoiceList : Fragment() {

    private var _binding : FragmentInvoiceListBinding? = null
    private val binding get() = _binding!!
    private val viewmodel: InvoiceListViewModel by viewModels()
    private lateinit var adapter: FacturaAdapter
    private var start:Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentInvoiceListBinding.inflate(inflater,container,false)
        binding.viewmodel = this.viewmodel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_invoiceList_to_invoiceCreation)
        }

        initRecyclerView()
        viewmodel.getInvoiceList()
        viewmodel.getState().observe(viewLifecycleOwner, Observer {
            when (it) {
                InvoiceListState.NoDataSet -> showNoData()
                is InvoiceListState.Success -> onSuccess(it.dataset)

            }
        })
    }

    fun initRecyclerView() {
        adapter = FacturaAdapter(
            onClickListener = {Factura -> onItemSelected(Factura)},
            onClickDelete = {position -> onDeleteItem(position)}
        )
        binding.invoiceListRvFacturas.layoutManager = LinearLayoutManager(requireContext())
        binding.invoiceListRvFacturas.adapter = adapter
    }
    private fun onDeleteItem(position: Int) {
        adapter.deleteInvoice(position)
        if(adapter.itemCount == 0) {
            binding.invoiceCreationLayoutVacio.visibility = View.VISIBLE
            binding.invoiceListRvFacturas.visibility = View.GONE
        }
    }
    private fun onItemSelected(invoice: Factura) {
        findNavController().navigate(InvoiceListDirections.actionInvoiceListToInvoiceDetail(invoice))

    }
    private fun onSuccess(dataset: ArrayList<Factura>) {
        binding.invoiceCreationLayoutVacio.visibility = View.GONE
        binding.invoiceListRvFacturas.visibility = View.VISIBLE
        adapter.update(dataset)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onStart() {
        super.onStart()
        if (start) {
            viewmodel.getInvoiceList()
            start = false
        }
    }

    private fun showNoData() {
        binding.invoiceListRvFacturas.visibility = View.GONE
        binding.invoiceCreationLayoutVacio.visibility = View.VISIBLE
    }
}