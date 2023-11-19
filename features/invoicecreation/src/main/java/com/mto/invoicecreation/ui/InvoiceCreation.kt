package com.mto.invoicecreation.ui

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.moronlu18.invoicecreation.R
import com.moronlu18.invoicecreation.databinding.FragmentInvoiceCreationBinding
import com.mto.invoicecreation.adapter.ItemAdapter
import com.mto.invoicecreation.data.Item
import com.mto.invoicecreation.data.ItemProvider
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class InvoiceCreation : Fragment() {

    private var _binding: FragmentInvoiceCreationBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentInvoiceCreationBinding.inflate(inflater, container, false)

        return binding.root;
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initReciclerView() {
        val manager = LinearLayoutManager(requireContext())

        binding.invoiceCreationRvDisponibles.layoutManager = manager
        binding.invoiceCreationRvDisponibles.adapter = ItemAdapter(ItemProvider.itemList) { item ->
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