package com.cbo.customerlist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cbo.customerlist.adapter.ClientesAdapter
import com.cbo.customerlist.data.model.Clientes
import com.cbo.customerlist.data.repository.ClientesProvider
import com.moronlu18.invoice.R
import com.moronlu18.customerlist.databinding.FragmentCustomerListBinding

class CustomerList : Fragment() {

    private var _binding: FragmentCustomerListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCustomerListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerViewClientes()

        binding.customerListFltbtnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_CustomerListFragment_to_CustomerCreationFragment)
        }
    }

    private fun initRecyclerViewClientes() {

        val manager = LinearLayoutManager(requireContext())
        val decoration = DividerItemDecoration(requireContext(), manager.orientation)
        binding.customerListRvClientes.layoutManager = manager


        val recyclerView = binding.customerListRvClientes
        val emptyTextView = binding.customerListTvempty

        if (ClientesProvider.clientesListVacia.isEmpty()) {
            emptyTextView.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            emptyTextView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }

        binding.customerListRvClientes.adapter =
            ClientesAdapter(ClientesProvider.clientesListVacia) {

                    x ->
                onItemSelected(x)
            }
        binding.customerListRvClientes.addItemDecoration(decoration)

    }

    fun onItemSelected(cliente: Clientes) {
        //Toast.makeText(requireContext(),cliente.name, Toast.LENGTH_SHORT).show()
        findNavController().navigate(com.moronlu18.invoice.R.id.action_CustomerListFragment_to_CustomerDetailFragment)
        //findNavController().navigate(Customer)
        //findNavController().navigate(CustomerListDirections.actionCustomerListFragmentToCustomerDetailFragment)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}