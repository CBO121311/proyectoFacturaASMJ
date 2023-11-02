package com.sergiogv98.taskcreation

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.moronlu18.taskcreation.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivityPrueba : AppCompatActivity() {

    private lateinit var btnDatePicker : Button
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_task_creation)

        btnDatePicker = findViewById(R.id.task_Creation_button_date_creation)

        btnDatePicker.setOnClickListener {
            Log.d("MiApp", "Botón de fecha clickeado")
            showDatePicker()
        }
    }

    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(this, {datePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, monthOfYear, dayOfMonth)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(selectedDate.time)
            btnDatePicker.text=formattedDate
        },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.show()
    }

}