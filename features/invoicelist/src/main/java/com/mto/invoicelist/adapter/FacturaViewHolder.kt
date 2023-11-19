package com.mto.invoicelist.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mto.invoicelist.data.Factura
import com.moronlu18.invoicelist.databinding.ItemFacturaBinding

class FacturaViewHolder(view: View): RecyclerView.ViewHolder(view){

    private val binding = ItemFacturaBinding.bind(view)

    fun render(
        facturaModel: Factura,
        onClickListener: (Factura) -> Unit,
        onClickDelete: (Int) -> Unit
    ) {

        binding.itemFacturaIvtTotal.text ="Total: ${facturaModel.total}"
        binding.itemFacturaTvId.text = facturaModel.id.toString()
        binding.itemFacturaTvCliente.text =facturaModel.cliente

        itemView.setOnClickListener { onClickListener(facturaModel) }
        binding.invoiceItemBtnDelete.setOnClickListener { onClickDelete(adapterPosition) }


    }
}