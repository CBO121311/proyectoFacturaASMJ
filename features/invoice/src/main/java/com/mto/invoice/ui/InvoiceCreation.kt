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
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.moronlu18.accounts.entity.Item
import com.moronlu18.data.base.CustomerId
import com.moronlu18.data.base.InvoiceId
import com.moronlu18.data.invoice.LineItem
import com.moronlu18.data.invoice.Invoice
import com.moronlu18.data.invoice.InvoiceStatus
import com.moronlu18.invoice.ui.MainActivity
import com.moronlu18.invoicelist.R
import com.moronlu18.invoicelist.databinding.FragmentInvoiceCreationBinding
import com.mto.invoice.adapter.creation.AddItemCreationAdapter
import com.mto.invoice.adapter.creation.CreationAdapter
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
    private lateinit var adapter: CreationAdapter
    private var _ItemSelected: Item? = null;
    private val ItemSelected get() = _ItemSelected!!

    private var InvoiceSelected by Delegates.notNull<Int>()

    private var itemMutableList: MutableList<Item> = mutableListOf()
    private lateinit var manager2: LinearLayoutManager
    private lateinit var invoiceEdit: Invoice


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
                val idFactura =  result.getInt("position")
                InvoiceSelected = idFactura
                invoiceEdit = viewmodel.getInvoiceById(idFactura)
                viewmodel.setEditorMode(true)
                binding.invoiceCreationTieCliente.setText(invoiceEdit.customerId.value.toString())
                val formatoFecha =
                    DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault())
                binding.invoiceCreationTieFechaEm.setText(formatoFecha.format(invoiceEdit.issuedDate))
                binding.invoiceCreationTieFechaFin.setText(formatoFecha.format(invoiceEdit.dueDate))
                itemMutableList = viewmodel.getListItemByInvoiceId(invoiceEdit.id) as MutableList<Item>
                initReciclerView()
                binding.invoiceCreationTvTotalText.setText(viewmodel.giveTotal(itemMutableList as List<Item>))
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
                InvoiceCreationState.AtLeastOneItem -> setAtLeastOneItem(view)
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
        initRecyclerItems()
        viewmodel.allItems.observe(viewLifecycleOwner) {
            it.let { adapter.submitList(it) }
        }


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

    /**
     * Función que inicializa el adapter de items disponibles, que están en la base de datos
     * del proyecto
     */
    fun initRecyclerItems() {
        adapter = CreationAdapter (
            onClickListener = { item -> onItemSelected(item) },
            )
        val manager = LinearLayoutManager(requireContext())
        binding.invoiceCreationRvDisponibles.layoutManager = manager
        binding.invoiceCreationRvDisponibles.adapter = adapter

    }

    private fun parse(num: Int): InvoiceStatus {
        if (num == 0) {
            return InvoiceStatus.PENDIENTE
        } else if (num == 1) {
            return InvoiceStatus.PAGADA
        } else {
            return InvoiceStatus.VENCIDA
        }

    }

    private fun onSuccessCreate() {
        val customId = binding.invoiceCreationTieCliente.text.toString().toInt()
        val stat = binding.invoiceCreationSpEstado.selectedItemPosition
        val issued = parseStringToInstant(binding.invoiceCreationTieFechaEm.text.toString())
        val due = parseStringToInstant(binding.invoiceCreationTieFechaFin.text.toString())
        lateinit var lineItems: List<LineItem>
        var idInvoice by Delegates.notNull<Int>()
        if (viewmodel.getEditorMode()) {
            idInvoice = invoiceEdit.id.value
            lineItems = createListLineItems(idInvoice, itemMutableList)
            val editInvoice = Invoice(
                id = InvoiceId(idInvoice),
                customerId = CustomerId(customId),
                number = invoiceEdit.number,
                status = parse(stat),
                issuedDate = issued,
                dueDate = due,
                lineItems = createListLineItems(idInvoice, itemMutableList)
            )
            viewmodel.editRepository(editInvoice)
            addLineItems(lineItems)
        } else {
            idInvoice = viewmodel.giveId() + 1
            lineItems = createListLineItems(idInvoice, itemMutableList)
            val invoice = Invoice(
                id = InvoiceId(idInvoice),
                customerId = CustomerId(customId),
                number = viewmodel.giveNumber(),
                status = parse(stat),
                issuedDate = issued,
                dueDate = due,
                lineItems = createListLineItems(idInvoice, itemMutableList)
            )
            viewmodel.addRepository(invoice)
            addLineItems(lineItems)
        }

        findNavController().popBackStack()
    }

    private fun addLineItems(lista: List<LineItem>) {
        for (item in lista) {
            viewmodel.insertLineItem(item)
        }
    }

    /**
     * Funcion que crea la lista de line_item para añadirla a la factura, usamos una lista de objetos
     * unicos para crear los line_items y la lista original de items, esto para conocer la cantidad
     * de veces que se repiten los items y aumentar la cantidad y el precio del objeto creado
     */
    private fun createListLineItems(id: Int, itemMutableList: MutableList<Item>): List<LineItem> {
        val listaUnicos: List<Item> = itemMutableList.distinctBy { it.id }
        val lista: MutableList<LineItem> = mutableListOf()
        var creacion: LineItem
        if (listaUnicos.size == itemMutableList.size) {
            for (item in listaUnicos) {
                creacion = LineItem(
                    invoiceId = InvoiceId(id),
                    itemId = item.id,
                    quantity = 1,
                    price = item.price,
                    iva = item.vat.iva
                )
                lista.add(creacion)
            }
            return lista
        } else {
            var primerObjeto = true
            for (item in listaUnicos) {
                creacion = LineItem(
                    invoiceId = InvoiceId(id),
                    itemId = item.id,
                    quantity = 1,
                    price = item.price,
                    iva = item.vat.iva
                )
                for (itemMutable in itemMutableList.sortedBy { it.id }) {
                    if (item.id == itemMutable.id) {
                        if(!primerObjeto) {
                            creacion.quantity++;
                            creacion.price = creacion.price + item.price;
                        }
                        primerObjeto = false

                    }
                }
                lista.add(creacion);
                primerObjeto = true
            }
            return lista
        }


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

    /**
     * Funcion que añade un item al recycler view de items añadidos
     */
    private fun addItem() {
        if (!viewmodel.getEditorMode()) {
            if (itemMutableList.size != 0) {
                binding.invoiceCreationRvErrorAnadidos.text = ""
            }
            val manager2 = LinearLayoutManager(requireContext())
            binding.invoiceCreationRvAnadidos.layoutManager = manager2
            binding.invoiceCreationRvAnadidos.adapter = AddItemCreationAdapter(itemMutableList)
            binding.viewmodel?.adapter?.value = ItemCreationAdapter(itemMutableList) { item ->
                onItemSelected(
                    item
                )
            }
            itemMutableList.add(ItemSelected)
            binding.invoiceCreationRvAnadidos.adapter!!.notifyDataSetChanged()
            binding.invoiceCreationRvErrorAnadidos.text = ""
            binding.invoiceCreationTvTotalText.text = viewmodel.giveTotal(itemMutableList)
        } else {
            val nuevaLista: MutableList<Item> = mutableListOf()
            for (item in itemMutableList) {
                nuevaLista.add(item)
            }
            nuevaLista.add(ItemSelected)
            itemMutableList = nuevaLista
            val manager2 = LinearLayoutManager(requireContext())
            binding.invoiceCreationRvAnadidos.layoutManager = manager2
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
            binding.invoiceCreationRvAnadidos.adapter!!.notifyDataSetChanged()
            binding.invoiceCreationRvErrorAnadidos.text = ""
            binding.invoiceCreationTvTotalText.text = viewmodel.giveTotal(itemMutableList)
        }
    }

    /**
     * Funcion que borra un item del recycler view de items añadidos
     */
    private fun deleteItem() {
        if (!viewmodel.getEditorMode()) {
            manager2 = LinearLayoutManager(requireContext())
            itemMutableList.remove(ItemSelected)
            binding.invoiceCreationRvAnadidos.layoutManager = manager2
            binding.invoiceCreationRvAnadidos.adapter =
                ItemCreationAdapter(itemMutableList) { item ->
                    onItemSelected(
                        item
                    )
                }

            binding.invoiceCreationRvAnadidos.adapter!!.notifyDataSetChanged()
            binding.invoiceCreationRvErrorAnadidos.text = ""
            binding.invoiceCreationTvTotalText.text = viewmodel.giveTotal(itemMutableList)
        } else {
            val nuevaLista: MutableList<Item> = mutableListOf()
            var borrado = false
            for (item in itemMutableList) {
                if (!borrado) {
                    if (item != ItemSelected) {
                        nuevaLista.add(item)
                    } else {
                        borrado = true
                    }
                } else {
                    nuevaLista.add(item)
                }

            }
            itemMutableList = nuevaLista
            val manager2 = LinearLayoutManager(requireContext())
            binding.invoiceCreationRvAnadidos.layoutManager = manager2
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

    private fun setAtLeastOneItem(view: View) {
        Snackbar.make(view,getString(R.string.errAtLeastOneItem), Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
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

