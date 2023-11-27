package com.mto.invoice.ui

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.moronlu18.accounts.entity.Item
import com.moronlu18.accounts.repository.CustomerProvider
import com.moronlu18.accounts.repository.ItemProvider
import com.moronlu18.invoicelist.R
import com.moronlu18.invoicelist.databinding.FragmentInvoiceCreationBinding
import com.mto.invoice.adapter.ItemCreationAdapter


import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class InvoiceCreation : Fragment() {

    private var _binding: FragmentInvoiceCreationBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentInvoiceCreationBinding.inflate(inflater, container, false)

        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.invoiceCreationTieFechaEm.setOnClickListener {
            showDatePicker(binding.invoiceCreationTieFechaEm)
        }
        binding.invoiceCreationTieFechaFin.setOnClickListener {
            showDatePicker(binding.invoiceCreationTieFechaFin)
        }
        binding.invoiceCreationFabFactura.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager

            fragmentManager.popBackStack()
        }
        initReciclerView()

        val spinnerCliente: Spinner = binding.invoiceCreationSpinnerCliente
        val customerList = CustomerProvider.dataSet

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            customerList.map { "${it.id} - ${it.name}" }.toTypedArray()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCliente.adapter = adapter

        spinnerCliente.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                val selectedCustomer = customerList[position]
                if (selectedCustomer != null) {
                    Toast.makeText(
                        requireContext(),
                        "${selectedCustomer.name}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {
            }
        }



    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initReciclerView() {
        val manager = LinearLayoutManager(requireContext())

        binding.invoiceCreationRvDisponibles.layoutManager = manager
        binding.invoiceCreationRvDisponibles.adapter = ItemCreationAdapter(ItemProvider.itemList) { item ->
            onItemSelected(
                item
            )
        }
    }
    fun onItemSelected(item: Item) {
            Toast.makeText(requireContext(), item.name, Toast.LENGTH_SHORT).show()


    }

    private val calendar = Calendar.getInstance()
    private fun showDatePicker(componente: TextInputEditText) {
        val datePickerDialog = DatePickerDialog(
            requireContext(), R.style.invoice_creation_calendarStyle ,{ DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int -> val  selectedDate= Calendar.getInstance()
                selectedDate.set(year,monthOfYear,dayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)
                componente.setText(formattedDate)

            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)

        )
        datePickerDialog.show();

    }


}