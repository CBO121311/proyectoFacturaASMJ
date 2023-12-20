package com.mto.invoice.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.moronlu18.accounts.entity.Factura
import com.moronlu18.accounts.repository.CustomerProvider
import com.moronlu18.invoicelist.databinding.ItemFacturaBinding

class FacturaViewHolder(view: View): RecyclerView.ViewHolder(view){

    private val binding = ItemFacturaBinding.bind(view)

    fun render(
        facturaModel: Factura,
        onClickListener: (Factura) -> Unit,
        onClickDelete:  ((Int) -> Unit)? = null,
    ) {

        binding.itemFacturaIvtTotal.text ="Total: ${facturaModel.number}"
        binding.itemFacturaTvId.text = facturaModel.id.toString()
        binding.itemFacturaTvCliente.text =CustomerProvider.getNom(facturaModel.customerId)


        //Todo Cambiado por el tema de la foto.

        val customer = CustomerProvider.getCustomerbyID(facturaModel.customerId)

        if (customer?.phototrial != null) {
            binding.itemFacturaIvKiwi.setImageResource(customer.phototrial!!)
        } else {
            binding.itemFacturaIvKiwi.setImageBitmap(customer?.photo)
        }
        //binding.itemFacturaIvKiwi.setImageResource(CustomerProvider.getPhoto(facturaModel.customerId))


        itemView.setOnClickListener { onClickListener?.invoke(facturaModel) }
        binding.invoiceItemBtnDelete.setOnClickListener {
            onClickDelete?.invoke(
                adapterPosition
            )
        }


    }
}