package com.mto.invoice.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.moronlu18.invoice.R
import com.moronlu18.invoicelist.databinding.FragmentInvoiceDetailBinding
import com.mto.invoice.adapter.ItemAdapter
import com.mto.invoice.data.model.Item
import com.mto.invoice.data.repository.ItemProvider


class InvoiceDetail : Fragment() {


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

        return binding.root;

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initReciclerView()

        binding.invoiceDetailFabCheck.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager

            fragmentManager.popBackStack()
        }

    }

    private fun initReciclerView() {
        val manager = LinearLayoutManager(requireContext())

        binding.invoiceDetailRvArticulos.layoutManager = manager
        binding.invoiceDetailRvArticulos.adapter = ItemAdapter(ItemProvider.itemList) { item ->
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