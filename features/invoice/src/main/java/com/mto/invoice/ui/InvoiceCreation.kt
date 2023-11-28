package com.mto.invoice.ui


import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.moronlu18.accounts.entity.Factura
import com.moronlu18.accounts.entity.InvoiceStatus
import com.moronlu18.accounts.entity.Item
import com.moronlu18.accounts.repository.CustomerProvider
import com.moronlu18.accounts.repository.FacturaProvider
import com.moronlu18.accounts.repository.ItemProvider
import com.moronlu18.invoicelist.R
import com.moronlu18.invoicelist.databinding.FragmentInvoiceCreationBinding
import com.mto.invoice.adapter.AddItemCreationAdapter
import com.mto.invoice.adapter.ItemCreationAdapter
import com.mto.invoice.usecase.InvoiceCreationState
import com.mto.invoice.usecase.InvoiceCreationViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class InvoiceCreation : Fragment() {
    inner class Listener : TextWatcher {
        private val contenedor: TextInputLayout
        constructor(contenedor: TextInputLayout) {
            this.contenedor = contenedor;
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            contenedor.isErrorEnabled = false
        }

        override fun afterTextChanged(s: Editable?) {
        }

    }

    private var _binding: FragmentInvoiceCreationBinding? = null
    private val binding get() = _binding!!
    private val viewmodel: InvoiceCreationViewModel by viewModels()

    private var _ItemSelected: Item? = null;
    private val ItemSelected get() = _ItemSelected!!

    private var itemMutableList: MutableList<Item> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentInvoiceCreationBinding.inflate(inflater, container, false)
        binding.viewmodel = this.viewmodel
        binding.lifecycleOwner = this
        binding.invoiceCreationTieCliente.addTextChangedListener(Listener(binding.invoiceCreationTilCliente))
        binding.invoiceCreationTieFechaEm.addTextChangedListener(Listener(binding.invoiceCreationTilFechaEm))
        binding.invoiceCreationTieFechaFin.addTextChangedListener(Listener(binding.invoiceCreationTilFechaFin))
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

        viewmodel.getState().observe(viewLifecycleOwner, Observer {
            when (it) {
                InvoiceCreationState.CustomerUnspecified -> setCustomerUnspecified()
                InvoiceCreationState.AtLeastOneItem -> setAtLeastOneItem()
                InvoiceCreationState.CustomerNoExists -> setCustomerNoExists()
                InvoiceCreationState.StartDateEmptyError -> setStartDateEmpty()
                InvoiceCreationState.EndDateEmptyError -> setEndDateEmpty()
                InvoiceCreationState.IncorrectDateRange -> setIncorrectDateRange()
                InvoiceCreationState.EndDateFormatError -> setEndDateFormatError()
                InvoiceCreationState.EndDateFormatError -> setEndDateFormatError()
                InvoiceCreationState.StartDateFormatError -> setStartDateFormatError()
                InvoiceCreationState.OnSuccess -> onSuccessCreate()
            }
        })


            binding.invoiceCreationBtnArticulo.setOnClickListener {
                if(_ItemSelected!=null) {
                    addItem()
                }
            }

        initReciclerView()
    }
    private fun parse(text:String):InvoiceStatus {
        if ("Pagada".equals(text)) {
            return InvoiceStatus.Pagada
        } else if ("Vencida".equals(text)) {
            return InvoiceStatus.Vencida
        } else {
            return InvoiceStatus.Pendiente
        }

    }
    private fun onSuccessCreate() {
        val customId = CustomerProvider.getId(binding.invoiceCreationTieCliente.text.toString())
        val numb = ItemProvider.getTotal(itemMutableList).replace(',','.')
        val stat = binding.invoiceCreationSpEstado.selectedItem.toString()
        val issued = binding.invoiceCreationTieFechaEm.text.toString()
        val due = binding.invoiceCreationTieFechaFin.text.toString()
        val items = itemMutableList.toList()


        val invoice = Factura(
            id = FacturaProvider.dataSet.size + 1,
            customerId = customId,
            number = numb.subSequence(0,numb.length -1).toString().toDouble(),
            status = parse(stat),
            issuedDate = issued,
            dueDate = due,
            lineItems = items

        )
        FacturaProvider.dataSet.add(invoice)
        findNavController().popBackStack()
    }

    private fun addItem() {
        if(itemMutableList.size != 0) {
            binding.invoiceCreationRvErrorAnadidos.text = ""
        }
        val manager2 = LinearLayoutManager(requireContext())
        binding.invoiceCreationRvAnadidos.layoutManager = manager2
        binding.invoiceCreationRvAnadidos.adapter = AddItemCreationAdapter(itemMutableList, 1)
        binding.viewmodel?.adapter?.value = AddItemCreationAdapter(itemMutableList,1)
        if(ItemSelected == null) {

        } else {
            //PINCHAR EL MISMO ITEM, CAMBIAR LA CANTIDAD DE UNIDADES EN EL ITEM VISIBLE, FALTA PICAR
            /*if(itemMutableList.contains(ItemSelected)) {
                var iterador = 0
                var acumulador = 1
                var primeraAparicion:Int = -1
                for (item in itemMutableList) {
                    if(item == ItemSelected) {
                        if(primeraAparicion == -1) {
                            primeraAparicion = iterador
                        }
                        acumulador++
                    }else{
                        iterador++
                    }
                }
                itemMutableList.removeAt(primeraAparicion)
                binding.invoiceCreationRvAnadidos.adapter = AddItemCreationAdapter(itemMutableList, acumulador)
                itemMutableList.add(index = primeraAparicion,ItemSelected)
                binding.invoiceCreationRvAnadidos.adapter!!.notifyDataSetChanged()
            } else {
                itemMutableList.add(ItemSelected)
                binding.invoiceCreationRvAnadidos.adapter = AddItemCreationAdapter(itemMutableList, 1)
                binding.invoiceCreationRvAnadidos.adapter!!.notifyDataSetChanged()
            }*/
            itemMutableList.add(ItemSelected)
            binding.invoiceCreationRvAnadidos.adapter!!.notifyDataSetChanged()
            binding.invoiceCreationRvErrorAnadidos.text = ""
            binding.invoiceCreationTvTotalText.text = ItemProvider.getTotal(itemMutableList).toString()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initReciclerView() {
        val manager = LinearLayoutManager(requireContext())

        binding.invoiceCreationRvDisponibles.layoutManager = manager
        binding.invoiceCreationRvDisponibles.adapter = ItemCreationAdapter(ItemProvider.dataSetItem) { item ->
            onItemSelected(
                item
            )
        }
    }

    fun onItemSelected(item: Item) {
        _ItemSelected = item

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

    private fun setCustomerUnspecified() {
        binding.invoiceCreationTilCliente.error = getString(R.string.errUserEmpty)
        binding.invoiceCreationTieCliente.requestFocus()

    }
    private fun setAtLeastOneItem() {
        binding.invoiceCreationRvErrorAnadidos.text = getString(R.string.errAtLeastOneItem)
    }
    private fun setCustomerNoExists() {
        binding.invoiceCreationTilCliente.error = getString(R.string.errUserNoExists)
        binding.invoiceCreationTieCliente.requestFocus()
    }

    private fun setStartDateEmpty() {
        binding.invoiceCreationTilFechaEm.error = getString(R.string.errStartDateEmpty)
        binding.invoiceCreationTieFechaEm.requestFocus()


    }
    private fun setEndDateEmpty() {
        binding.invoiceCreationTilFechaFin.error = getString(R.string.errEndDateEmpty)
        binding.invoiceCreationTieFechaFin.requestFocus()
    }

    private fun setIncorrectDateRange() {
        binding.invoiceCreationTilFechaFin.error = getString(R.string.errIncorrectDateRange)
        binding.invoiceCreationTieFechaFin.requestFocus()
    }
    private fun setStartDateFormatError() {
        binding.invoiceCreationTilFechaEm.error = getString(R.string.errDateFormat)
        binding.invoiceCreationTieFechaEm.requestFocus()
    }

    private fun setEndDateFormatError() {
        binding.invoiceCreationTilFechaFin.error =  getString(R.string.errDateFormat)
        binding.invoiceCreationTieFechaFin.requestFocus()
    }


}

