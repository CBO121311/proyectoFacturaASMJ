package com.cbo.customer.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cbo.customer.adapter.CustomerAdapter
import com.moronlu18.accounts.entity.Customer
import com.moronlu18.accounts.repository.CustomerProvider
import com.moronlu18.customercreation.R

import com.moronlu18.customercreation.databinding.FragmentCustomerListBinding

class CustomerList : Fragment() {

    private var _binding: FragmentCustomerListBinding? = null
    private val binding get() = _binding!!
    private var customerMutableList: MutableList<Customer> = CustomerProvider.dataSet
    private lateinit var adapter: CustomerAdapter
    private var isDeleting = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCustomerListBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
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


    /**
     * Inicia el recycleview
     */
    private fun initRecyclerViewClientes() {
        adapter = CustomerAdapter(
            clientesList = customerMutableList,
            onClickListener = { cliente -> onItemSelected(cliente) },
            onClickDelete = { position -> onDeletedItem(position) })

        updateEmptyView()

        binding.customerListRvClientes.layoutManager = LinearLayoutManager(requireContext())
        binding.customerListRvClientes.adapter = adapter
    }


    /**
     * Comprueba si la lista está vacía.
     */
    private fun updateEmptyView() {

        if (customerMutableList.isEmpty()) {
            //binding.customerListTvempty.visibility = View.VISIBLE
            binding.llListEmpty.visibility = View.VISIBLE
        } else {
            binding.llListEmpty.visibility = View.GONE
        }
    }

    /**
     * Acción al eliminar un elemento del recycle.
     */

    private fun onDeletedItem(position: Int) {

        if (!isDeleting) {
            isDeleting = true
            customerMutableList.removeAt(position)
            adapter.notifyItemRemoved(position)
        }

        binding.customerListRvClientes.postDelayed({
            isDeleting = false
        }, 300)

        updateEmptyView()
    }


    /**
     *  Envía un objeto customer al layout customerDetail utilizando SafeArgs
     */
    private fun onItemSelected(customer: Customer) {
        findNavController().navigate(
            CustomerListDirections.actionCustomerListToCustomerDetail(
                customer
            )
        )
    }


    /**
     * Creación del menu de customer_list
     */
    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_customer_list, menu)
    }


    /**
     * Opciones al seleccionar el menú. Actualmente solo hace el orden de la lista.
     */

    @SuppressLint("NotifyDataSetChanged")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_cd_action_sortname -> {
                customerMutableList.sortBy { it.name }
                adapter.notifyDataSetChanged()
                return true
            }

            R.id.menu_cd_action_sortid -> {
                customerMutableList.sortBy { it.id }
                adapter.notifyDataSetChanged()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    //region Métodos de pruebas
    /**
     * Comparador por nombre, actualmente descartado
     */
    private val customerNameComparator = Comparator<Customer> { customer1, customer2 ->
        customer1.name.compareTo(customer2.name)
    }

    /**
     * Crea un cliente
     * Fines de prueba.
     */
    private fun createCliente() {

        val clientes = Customer(
            3,
            "Tienda de verduras",
            "abc@gmail.com",
            photo = R.drawable.kiwidiner_background
        );
        customerMutableList.add(clientes)
        adapter.notifyItemInserted(customerMutableList.size - 1)
        //LinearLayoutManager(requireContext()).scrollToPositionWithOffset(clientesMutableList.size-1, 4)
        //binding.customerListRvClientes.layoutManager?.scrollToPosition(clientesMutableList.size - 1)
    }

    //endregion
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}