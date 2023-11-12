package com.sergiogv98.taskcreation

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.moronlu18.taskcreation.databinding.FragmentTaskCreationBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private val calendar = Calendar.getInstance()
class TaskCreation : Fragment() {

    private var _binding:FragmentTaskCreationBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.taskCreationButtonDateCreation.setOnClickListener {
            showDatePicker(binding.taskCreationButtonDateCreation)
        }

        binding.taskCreationButtonDateEnd.setOnClickListener {
            showDatePicker(binding.taskCreationButtonDateEnd)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTaskCreationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showDatePicker(button: Button) {
        val datePickerDialog = DatePickerDialog(
            requireContext(), { DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int -> val  selectedDate= Calendar.getInstance()
                selectedDate.set(year,monthOfYear,dayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)
                button.text = formattedDate
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)

        )
        datePickerDialog.show();

    }


}