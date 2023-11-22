package com.moronlu18.customer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.moronlu18.customer.adapter.ClientesAdapter
import com.moronlu18.accounts.entity.Clientes
import com.moronlu18.accounts.repository.ClientesProvider
import com.moronlu18.customercreation.R

import com.moronlu18.customercreation.databinding.FragmentCustomerListBinding

class CustomerList : Fragment() {

    private var _binding: FragmentCustomerListBinding? = null
    private val binding get() = _binding!!
    private var clientesMutableList: MutableList<Clientes> =
        ClientesProvider.clientesList.toMutableList()
    private lateinit var adapter: ClientesAdapter
    private var isDeleting = false


    // private lateinit var llmanager = LinearLayoutManager(requireContext())

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
            //createCliente()
            findNavController().navigate(R.id.action_customerList_to_customerCreation)
        }
    }


    private fun initRecyclerViewClientes() {

        /*adapter = ClientesAdapter(clientesMutableList) {
                x ->
            onItemSelected(x)
        }*/

        adapter = ClientesAdapter(
            clientesList = clientesMutableList,
            onClickListener = { cliente -> onItemSelected(cliente) },
            onClickDelete = { position -> onDeletedItem(position) })


        //val decoration = DividerItemDecoration(requireContext(), manager.orientation)


        val recyclerView = binding.customerListRvClientes
        val emptyTextView = binding.customerListTvempty

        if (ClientesProvider.clientesList.isEmpty()) {
            emptyTextView.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            emptyTextView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }

        binding.customerListRvClientes.layoutManager = LinearLayoutManager(requireContext())
        binding.customerListRvClientes.adapter = adapter

        //binding.customerListRvClientes.addItemDecoration(decoration)
    }

    private fun createCliente() {

        val clientes = Clientes(
            3,
            "Tienda de verduras",
            "abc@gmail.com",
            photo = com.moronlu18.inovice.R.drawable.cbotuxedo
        );
        clientesMutableList.add(clientes)
        adapter.notifyItemInserted(clientesMutableList.size - 1)
        //LinearLayoutManager(requireContext()).scrollToPositionWithOffset(clientesMutableList.size-1, 4)

        binding.customerListRvClientes.layoutManager?.scrollToPosition(clientesMutableList.size - 1)


    }

    private fun onDeletedItem(position: Int) {

        if (!isDeleting) {
            isDeleting = true
            clientesMutableList.removeAt(position)
            adapter.notifyItemRemoved(position)
        }

        binding.customerListRvClientes.postDelayed({
            isDeleting = false
        }, 300)
    }

    private fun onItemSelected(cliente: Clientes) {
        //findNavController().navigate(R.id.action_customerList_to_customerDetail)
        findNavController().navigate(CustomerListDirections.actionCustomerListToCustomerDetail(cliente.id))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}