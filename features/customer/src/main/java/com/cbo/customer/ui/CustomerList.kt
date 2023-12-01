package com.cbo.customer.ui


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
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
import com.cbo.customer.usecase.TAG
import com.moronlu18.accounts.entity.Customer
import com.moronlu18.accounts.entity.Email
import com.moronlu18.accounts.repository.CustomerProvider
import com.moronlu18.customercreation.R

import com.moronlu18.customercreation.databinding.FragmentCustomerListBinding
import com.moronlu18.invoice.base.BaseFragmentDialog

class CustomerList : Fragment() {

    private var _binding: FragmentCustomerListBinding? = null
    private val binding get() = _binding!!
    private var customerList = CustomerProvider.CustomerdataSet
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
            clientesList = customerList,
            onClickListener = { cliente -> onItemSelected(cliente) },
            onClickDelete = { position -> onDeletedItem(position) },
            onClickEdit = { position -> onEditItem(position) })

        updateEmptyView()

        binding.customerListRvClientes.layoutManager = LinearLayoutManager(requireContext())
        binding.customerListRvClientes.adapter = adapter
    }

    private fun onEditItem(position: Int) {
        var bundle = Bundle();
        bundle.putInt("position", position)

        parentFragmentManager.setFragmentResult("customkey", bundle)
        findNavController().navigate(R.id.action_customerList_to_customerCreation)
    }


    /**
     * Comprueba si la lista está vacía.
     */
    private fun updateEmptyView() {

        if (customerList.isEmpty()) {
            binding.customerListClEmpty.visibility = View.VISIBLE
        } else {
            binding.customerListClEmpty.visibility = View.GONE
        }
    }

    /**
     * Acción al eliminar un elemento del recycle.
     */
    private fun onDeletedItem(position: Int) {

        if (!isDeleting) {
            isDeleting = true
            findNavController().navigate(
                CustomerListDirections.actionCustomerListToBaseFragmentDialog2(
                    getString(com.moronlu18.invoice.R.string.title_fragmentDialogExit),
                    getString(R.string.Content_deleteCustomer)
                )
            )

            parentFragmentManager.setFragmentResultListener(
                BaseFragmentDialog.request,
                viewLifecycleOwner
            ) { _, result ->
                val success = result.getBoolean(BaseFragmentDialog.result, false)
                if (success) {
                    val customer1 = customerList[position]
                    val existe = CustomerProvider.deleteCustomer(customer1)
                    Log.i(TAG, "El cliente: ${existe}")
                    if (!existe) {

                        customerList.removeAt(position)
                        adapter.notifyItemRemoved(position)
                        updateEmptyView()

                    } else {
                        showConfirmationDialog()
                    }


                }
            }
        }

        binding.customerListRvClientes.postDelayed({
            isDeleting = false
        }, 300)

    }

    private fun showConfirmationDialog() {

        AlertDialog.Builder(requireContext())
            .setTitle("Aviso")
            .setMessage("No puedes borrar un cliente referenciado a factura o tarea")
            .setNegativeButton("Entendido", null)
            .show()
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
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_customer_list, menu)
    }


    /**
     * Opciones al seleccionar el menú. Actualmente solo hace el orden de la lista.
     */


    @SuppressLint("NotifyDataSetChanged")
    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_cd_action_sortname -> {
                customerList.sortBy { it.name }
                adapter.notifyDataSetChanged()
                return true
            }

            R.id.menu_cd_action_sortid -> {
                customerList.sortBy { it.id }
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
            Email("abc@gmail.com"),
            photo = R.drawable.kiwidiner_background
        );
        customerList.add(clientes)
        adapter.notifyItemInserted(customerList.size - 1)
        //LinearLayoutManager(requireContext()).scrollToPositionWithOffset(clientesMutableList.size-1, 4)
        //binding.customerListRvClientes.layoutManager?.scrollToPosition(clientesMutableList.size - 1)
    }

    //endregion
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}