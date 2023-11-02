package com.cbo.customerlist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cbo.customerlist.adapter.ClientesAdapter
import com.cbo.customerlist.data.Clientes
import com.cbo.customerlist.data.ClientesProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.moronlu18.customerlist.databinding.FragmentCustomerListBinding

class CustomerList : Fragment() {

    private lateinit var binding : FragmentCustomerListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCustomerListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fab = requireActivity().findViewById<FloatingActionButton>(com.moronlu18.invoice.R.id.fab)
        fab.visibility = View.GONE

        initRecyclerViewClientes()
    }

    private fun initRecyclerViewClientes(){

        val manager = LinearLayoutManager(requireContext())
        val decoration = DividerItemDecoration(requireContext(),manager.orientation)
        binding.customerListRvClientes.layoutManager = manager

        binding.customerListRvClientes.adapter = ClientesAdapter(ClientesProvider.clientesList){

            x-> onItemSelected1(x)
        }


        binding.customerListRvClientes.addItemDecoration(decoration)


    }
    fun onItemSelected1(cliente: Clientes) {
        Toast.makeText(requireContext(),cliente.name, Toast.LENGTH_SHORT).show()

    }
}