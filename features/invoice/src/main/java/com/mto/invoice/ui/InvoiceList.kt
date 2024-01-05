package com.mto.invoice.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.moronlu18.invoicelist.R
import com.mto.invoice.adapter.list.FacturaAdapter
import com.moronlu18.accounts.entity.Factura
import com.moronlu18.invoice.ui.MainActivity
import com.moronlu18.invoicelist.databinding.FragmentInvoiceListBinding
import com.mto.invoice.usecase.InvoiceListState
import com.mto.invoice.usecase.InvoiceListViewModel


class InvoiceList : Fragment(), MenuProvider {

    private var _binding : FragmentInvoiceListBinding? = null
    private val binding get() = _binding!!
    private val viewmodel: InvoiceListViewModel by viewModels()
    private lateinit var adapter: FacturaAdapter
    private var start:Boolean = true

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
        setUpToolbar()
        setUpFab()
        initRecyclerView()

        viewmodel.getState().observe(viewLifecycleOwner) {
            when (it) {
                is InvoiceListState.Loading -> showProgressBar(it.value)
                InvoiceListState.NoDataSet -> showNoData()
                is InvoiceListState.Success -> onSuccess(it.dataset)
            }
        }
    }


    fun initRecyclerView() {
        adapter = FacturaAdapter(
            onClickListener = {factura -> navigateInvoiceDetail(viewmodel.getPosByInvoice(factura))},

        )
        binding.invoiceListRvFacturas.layoutManager = LinearLayoutManager(requireContext())
        binding.invoiceListRvFacturas.adapter = adapter
    }
/*    private fun onEditItem(position: Int) {
        val bundle = Bundle();
        bundle.putInt("detailposition", position)
        parentFragmentManager.setFragmentResult("detailkey", bundle)
        findNavController().navigate(R.id.action_invoiceList_to_invoiceCreation)

    }*/
    private fun navigateInvoiceDetail(position: Int) {

        val bundle = Bundle()
        bundle.putInt("detailposition", position)
        parentFragmentManager.setFragmentResult("detailkey", bundle)

        findNavController().navigate(R.id.action_invoiceList_to_invoiceDetail)
    }
    private fun onSuccess(dataset: ArrayList<Factura>) {
        binding.invoiceCreationLayoutVacio.visibility = View.GONE
        binding.invoiceListRvFacturas.visibility = View.VISIBLE
        adapter.update(dataset)
    }
    override fun onStart() {
        super.onStart()
        if (start) {
            viewmodel.getInvoiceList()
            start = false
        }else {
            viewmodel.getListWithoutLoading()
        }
    }

    private fun showNoData() {
        binding.invoiceListRvFacturas.visibility = View.GONE
        binding.invoiceCreationLayoutVacio.visibility = View.VISIBLE
    }

    private fun showLayoutEmpty() {
        binding.invoiceListRvFacturas.visibility = View.GONE
        binding.invoiceCreationLayoutVacio.visibility = View.GONE
    }
    private fun showProgressBar(value: Boolean) {
        if (value) {
            showLayoutEmpty()
            findNavController().navigate(R.id.action_invoiceList_to_fragmentProgressDialogKiwi)
        } else {
            findNavController().popBackStack()
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_invoice_list, menu)
    }
    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.menu_cd_action_order -> {
                adapter.sort()
                return true
            }
            R.id.menu_cd_action_refresh -> {
                viewmodel.getInvoiceList()
                return true

            }
            else -> false
        }
    }

    private fun setUpToolbar() {
        (requireActivity() as? MainActivity)?.toolbar?.apply {
            visibility = View.VISIBLE
        }
        val menuhost: MenuHost = requireActivity()
        menuhost.addMenuProvider(this, viewLifecycleOwner)
    }
    private fun setUpFab() {
        (requireActivity() as? MainActivity)?.fab?.apply {
            visibility = View.VISIBLE
            setImageResource(R.drawable.ic_action_add)
            setOnClickListener {
                findNavController().navigate(R.id.action_invoiceList_to_invoiceCreation)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}