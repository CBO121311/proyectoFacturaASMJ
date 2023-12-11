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
        binding.itemFacturaIvKiwi.setImageResource(CustomerProvider.getPhoto(facturaModel.customerId))
        itemView.setOnClickListener { onClickListener?.invoke(facturaModel) }
        binding.invoiceItemBtnDelete.setOnClickListener {
            onClickDelete?.invoke(
                adapterPosition
            )
        }


    }
}