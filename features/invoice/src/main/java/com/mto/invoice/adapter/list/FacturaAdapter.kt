package com.mto.invoice.adapter.list

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moronlu18.invoicelist.R
import com.moronlu18.data.invoice.Invoice
import com.moronlu18.data.invoice.InvoiceStatus
import com.moronlu18.invoicelist.databinding.ItemFacturaBinding
import com.moronlu18.repository.CustomerProvider

class FacturaAdapter(
    private val onClickListener: (Invoice) -> Unit
) : ListAdapter<Invoice, FacturaAdapter.FacturaViewHolder>(INVOICE_COMPARATOR) {


    /*  interface OnUserClick { //Este elemento es público
        fun userClick(user: User) //Pulsación corta
        fun userOnLongClick(user: User) //Larga

    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacturaViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context);
        return FacturaViewHolder(layoutInflater.inflate(R.layout.item_factura, parent, false))
    }

    override fun onBindViewHolder(holder: FacturaViewHolder, position: Int) {
        val item = getItem(position)
        holder.render(item, onClickListener)
    }

    fun sort() {
        val sortedInvoiceList = currentList.sortedBy { getCustomerName(it.customerId.value) }
        submitList(sortedInvoiceList)
    }

    private fun getCustomerName(customerId: Int): String? {
        return CustomerProvider.getCustomerNameById(customerId)
    }

    override fun getItemCount(): Int = currentList.size

    inner class FacturaViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemFacturaBinding.bind(view)

        fun render(
            invoiceModel: Invoice,
            onClickListener: (Invoice) -> Unit,

            ) {

            val customer = CustomerProvider.getCustomerbyID(invoiceModel.customerId.value)

            binding.itemFacturaIvtNumber.text = invoiceModel.number
            binding.itemFacturaTvId.text = invoiceModel.id.value.toString()
            binding.itemFacturaTvCliente.text = customer?.name
            with(binding.itemFacturaEstado) {
                text = giveStatusText(invoiceModel.status)
                setTextColor(setColorEstado(invoiceModel.status))

            }

            if (customer?.photo != null) {
                binding.itemFacturaIvKiwi.setImageURI(customer.photo)
            } else {
                binding.itemFacturaIvKiwi.setImageResource(R.drawable.kiwidinero)
            }

            itemView.setOnClickListener { onClickListener?.invoke(invoiceModel) }


        }

        /**
         * Funcion que devuelve un texto en funcion del tipo de factura
         */
        fun giveStatusText(tipo: InvoiceStatus): String {
            return if (tipo == InvoiceStatus.PENDIENTE) {
                itemView.context.getString(R.string.invoiceStatusPendiente)
            } else if (tipo == InvoiceStatus.PAGADA) {
                itemView.context.getString(R.string.invoiceStatusPagada)
            } else {
                itemView.context.getString(R.string.invoiceStatusVencida)
            }

        }

        /**
         * Función que devuelve un entero que representa un color,
         * en función del estado de la factura
         */
        fun setColorEstado(status: InvoiceStatus): Int {
            return if (status.equals(InvoiceStatus.PENDIENTE)) {
                Color.parseColor("#FF1100")
            } else if (status.equals(InvoiceStatus.PAGADA)) {
                Color.parseColor("#217C00")
            } else {
                Color.parseColor("#978303")
            }

        }
    }

    companion object{
        private val INVOICE_COMPARATOR = object:DiffUtil.ItemCallback<Invoice>() {
            override fun areItemsTheSame(oldItem: Invoice, newItem: Invoice): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Invoice, newItem: Invoice): Boolean {
                return oldItem.number == newItem.number
            }

        }
    }

}