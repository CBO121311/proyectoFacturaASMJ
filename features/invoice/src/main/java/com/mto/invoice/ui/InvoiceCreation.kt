package com.mto.invoice.ui


import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.moronlu18.accounts.entity.Factura
import com.moronlu18.accounts.entity.InvoiceStatus
import com.moronlu18.accounts.entity.Item
import com.moronlu18.invoice.ui.MainActivity
import com.moronlu18.invoicelist.R
import com.moronlu18.invoicelist.databinding.FragmentInvoiceCreationBinding
import com.mto.invoice.adapter.creation.AddItemCreationAdapter
import com.mto.invoice.adapter.creation.ItemCreationAdapter
import com.mto.invoice.usecase.InvoiceCreationState
import com.mto.invoice.usecase.InvoiceCreationViewModel
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import kotlin.properties.Delegates


class InvoiceCreation : Fragment() {

    private var _binding: FragmentInvoiceCreationBinding? = null
    private val binding get() = _binding!!

    private val viewmodel: InvoiceCreationViewModel by viewModels()

    private var _ItemSelected: Item? = null;
    private val ItemSelected get() = _ItemSelected!!

    private var InvoiceSelected by Delegates.notNull<Int>()
    private var itemMutableList: MutableList<Item> = mutableListOf()
    private lateinit var manager2: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentInvoiceCreationBinding.inflate(inflater, container, false)
        binding.viewmodel = this.viewmodel
        binding.lifecycleOwner = this
        binding.invoiceCreationTieCliente.addTextChangedListener(Listener(binding.invoiceCreationTilCliente))
        binding.invoiceCreationTieFechaEm.addTextChangedListener(ListenerDates(binding.invoiceCreationTilFechaEm))
        binding.invoiceCreationTieFechaFin.addTextChangedListener(ListenerDates(binding.invoiceCreationTilFechaFin))

        parentFragmentManager.setFragmentResultListener(
            "invoicekey", this, FragmentResultListener { _, result ->
                val posFactura: Int = result.getInt("position")
                InvoiceSelected = posFactura
                val invoiceEdit = viewmodel.getInvoicePos(posFactura)
                viewmodel.setEditorMode(true)
                binding.invoiceCreationTieCliente.setText(invoiceEdit.customer.id.toString())
                val formatoFecha =
                    DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault())
                binding.invoiceCreationTieFechaEm.setText(formatoFecha.format(invoiceEdit.issuedDate))
                binding.invoiceCreationTieFechaFin.setText(formatoFecha.format(invoiceEdit.dueDate))
                itemMutableList = invoiceEdit.lineItems as MutableList<Item>
                initReciclerView()
                binding.invoiceCreationTvTotalText.setText(invoiceEdit.number.toString() + "€")
            }
        )
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpFab()
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
                else -> {}
            }
        })


        binding.invoiceCreationBtnArticulo.setOnClickListener {
            if (_ItemSelected != null) {
                addItem()
            }
        }
        binding.invoiceCreationBtnDeleteArticulo.setOnClickListener {
            if (_ItemSelected != null) {
                deleteItem()
            }
        }

        initReciclerView()
    }

    private fun parse(text: String): InvoiceStatus {
        if ("Pagada".equals(text)) {
            return InvoiceStatus.Pagada
        } else if ("Vencida".equals(text)) {
            return InvoiceStatus.Vencida
        } else {
            return InvoiceStatus.Pendiente
        }

    }

    private fun onSuccessCreate() {
        val customId = binding.invoiceCreationTieCliente.text.toString().toInt()
        val numb = viewmodel.giveTotal(itemMutableList).replace(',', '.')
        val stat = binding.invoiceCreationSpEstado.selectedItem.toString()
        val issued = parseStringToInstant(binding.invoiceCreationTieFechaEm.text.toString())
        val due = parseStringToInstant(binding.invoiceCreationTieFechaFin.text.toString())
        val items = itemMutableList.toList()

        if(viewmodel.getEditorMode()) {
            val editInvoice = Factura(
                id = viewmodel.giveIdEditor(viewmodel.getInvoicePos(InvoiceSelected)),
                customer = viewmodel.getCustomerById(customId)!!,
                number = numb.subSequence(0, numb.length - 1).toString().toDouble(),
                status = parse(stat),
                issuedDate = issued,
                dueDate = due,
                lineItems = items
            )
            viewmodel.editRepository(editInvoice,InvoiceSelected)
        }else {
            val invoice = Factura(
                id = viewmodel.giveId() + 1,
                customer = viewmodel.getCustomerById(customId)!!,
                number = numb.subSequence(0, numb.length - 1).toString().toDouble(),
                status = parse(stat),
                issuedDate = issued,
                dueDate = due,
                lineItems = items

            )
            viewmodel.addRepository(invoice)
        }

        findNavController().popBackStack()
    }

    private fun parseStringToInstant(dateString: String): Instant {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        return try {
            val localDate = LocalDate.parse(dateString, formatter)
            localDate.atStartOfDay(ZoneOffset.UTC).toInstant()
        } catch (e: Exception) {
            Instant.now()
        }
    }

    private fun addItem() {
        if(!viewmodel.getEditorMode()) {
            if (itemMutableList.size != 0) {
                binding.invoiceCreationRvErrorAnadidos.text = ""
            }
            val manager2 = LinearLayoutManager(requireContext())
            binding.invoiceCreationRvAnadidos.layoutManager = manager2
            binding.invoiceCreationRvAnadidos.adapter = AddItemCreationAdapter(itemMutableList)
            binding.viewmodel?.adapter?.value =  ItemCreationAdapter(itemMutableList) { item ->
                onItemSelected(
                    item
                )
            }
            itemMutableList.add(ItemSelected)
            binding.invoiceCreationRvAnadidos.adapter!!.notifyDataSetChanged()
            binding.invoiceCreationRvErrorAnadidos.text = ""
            binding.invoiceCreationTvTotalText.text = viewmodel.giveTotal(itemMutableList)
        }else {
            val nuevaLista: MutableList<Item> = mutableListOf()
            for (item in itemMutableList) {
                    nuevaLista.add(item)
            }
            nuevaLista.add(ItemSelected)
            itemMutableList = nuevaLista
            val manager2 = LinearLayoutManager(requireContext())
            binding.invoiceCreationRvAnadidos.layoutManager = manager2
            binding.invoiceCreationRvAnadidos.adapter = ItemCreationAdapter(itemMutableList) { item ->
                onItemSelected(
                    item
                )
            }
            binding.viewmodel?.adapter?.value = ItemCreationAdapter(itemMutableList) { item ->
                onItemSelected(
                    item
                )
            }
            binding.invoiceCreationRvAnadidos.adapter!!.notifyDataSetChanged()
            binding.invoiceCreationRvErrorAnadidos.text = ""
            binding.invoiceCreationTvTotalText.text = viewmodel.giveTotal(itemMutableList)
        }
    }

    private fun deleteItem() {
        if(!viewmodel.getEditorMode()) {
            manager2 = LinearLayoutManager(requireContext())
            itemMutableList.remove(ItemSelected)
            binding.invoiceCreationRvAnadidos.layoutManager = manager2
            binding.invoiceCreationRvAnadidos.adapter = ItemCreationAdapter(itemMutableList) { item ->
                    onItemSelected(
                        item
                    )
                }

            binding.invoiceCreationRvAnadidos.adapter!!.notifyDataSetChanged()
            binding.invoiceCreationRvErrorAnadidos.text = ""
            binding.invoiceCreationTvTotalText.text = viewmodel.giveTotal(itemMutableList)
        }else {
            val nuevaLista: MutableList<Item> = mutableListOf()
            var borrado = false
            for (item in itemMutableList) {
                if(!borrado){
                    if(item != ItemSelected){
                        nuevaLista.add(item)
                    }else {
                        borrado = true
                    }
                }else{
                    nuevaLista.add(item)
                }

            }
            itemMutableList = nuevaLista
            val manager2 = LinearLayoutManager(requireContext())
            binding.invoiceCreationRvAnadidos.layoutManager = manager2
            binding.invoiceCreationRvAnadidos.adapter = ItemCreationAdapter(itemMutableList) { item ->
                onItemSelected(
                    item
                )
            }
            binding.viewmodel?.adapter?.value = ItemCreationAdapter(itemMutableList) { item ->
                onItemSelected(
                    item
                )
            }
            binding.invoiceCreationRvAnadidos.adapter!!.notifyDataSetChanged()
            binding.invoiceCreationRvErrorAnadidos.text = ""
            binding.invoiceCreationTvTotalText.text = viewmodel.giveTotal(itemMutableList)
        }

    }

    private fun setUpFab() {
        (requireActivity() as? MainActivity)?.fab?.apply {
            visibility = View.GONE
        }
    }
    private fun initReciclerView() {
        if (viewmodel.getEditorMode()) {
            val manager = LinearLayoutManager(requireContext())
            binding.invoiceCreationRvAnadidos.layoutManager = manager
            binding.invoiceCreationRvAnadidos.adapter =
                ItemCreationAdapter(itemMutableList) { item ->
                    onItemSelected(
                        item
                    )
                }
            binding.viewmodel?.adapter?.value = ItemCreationAdapter(itemMutableList) { item ->
                onItemSelected(
                    item
                )
            }
        } else {
            val manager = LinearLayoutManager(requireContext())
            binding.invoiceCreationRvDisponibles.layoutManager = manager
            binding.invoiceCreationRvDisponibles.adapter =
                ItemCreationAdapter(viewmodel.giveListItem()) { item ->
                    onItemSelected(
                        item
                    )
                }
        }
    }

    fun onItemSelected(item: Item) {
        _ItemSelected = item

    }

    private val calendar = Calendar.getInstance()
    private fun showDatePicker(componente: TextInputEditText) {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            R.style.invoice_creation_calendarStyle,
            { DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
        binding.invoiceCreationTilFechaFin.error = getString(R.string.errDateFormat)
        binding.invoiceCreationTieFechaFin.requestFocus()
    }

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
            if (!s!!.isEmpty()) {
                binding.invoiceCreationBtnCliText.text = viewmodel.giveNom()

            }
        }

    }

    inner class ListenerDates : TextWatcher {
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


}

