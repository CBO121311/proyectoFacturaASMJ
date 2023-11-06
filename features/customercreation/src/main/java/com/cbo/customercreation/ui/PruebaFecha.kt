package com.cbo.customercreation.ui

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.moronlu18.customercreation.databinding.FragmentPruebaFechaBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class PruebaFecha : Fragment() {

    private var _binding:FragmentPruebaFechaBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnShowDatePicker.setOnClickListener {
            showDatePicker()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentPruebaFechaBinding.inflate(inflater, container, false)
        return binding.root
       }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val calendar = Calendar.getInstance()
    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(
            requireContext(), { DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int -> val  selectedDate= Calendar.getInstance()
                selectedDate.set(year,monthOfYear,dayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)
                binding.tvSelectDate.text = formattedDate
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)

        )
        datePickerDialog.show();

    }




}