package com.mto.invoicelist.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mto.invoicelist.data.Factura
import com.moronlu18.invoicelist.databinding.ItemFacturaBinding

class FacturaViewHolder(view: View): RecyclerView.ViewHolder(view){

    private val binding = ItemFacturaBinding.bind(view)

    fun render(facturaModel: Factura, onClickListener:(Factura) -> Unit) {
        binding.itemFacturaIvtFa.text = "Factura: "
        binding.itemFacturaItvNo.text = "Cliente: "
        binding.itemFacturaItvFem.text = "Fecha de emisión: "
        binding.itemFacturaItvFe.text = "Fecha de vencimiento: "

        binding.itemFacturaItvTotal.text ="Total: ${facturaModel.total}"
        binding.itemFacturaTvId.text = facturaModel.id.toString()
        binding.itemFacturaTvCliente.text =facturaModel.cliente
        binding.itemFacturaTvFechaEm.text =facturaModel.fechaE
        binding.itemFacturaTvFechaF.text =facturaModel.fechaF

        itemView.setOnClickListener { onClickListener(facturaModel) }


    }
}