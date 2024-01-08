package com.mto.invoice.adapter.list

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.moronlu18.accounts.entity.Factura

import com.moronlu18.invoicelist.databinding.ItemFacturaBinding

class FacturaViewHolder(view: View): RecyclerView.ViewHolder(view){

    private val binding = ItemFacturaBinding.bind(view)

    fun render(
        facturaModel: Factura,
        onClickListener: (Factura) -> Unit,

    ) {

        binding.itemFacturaIvtTotal.text ="Total: ${facturaModel.number}"
        binding.itemFacturaTvId.text = facturaModel.id.toString()
        binding.itemFacturaTvCliente.text =facturaModel.customer.name
        with(binding.itemFacturaEstado) {
            text = facturaModel.status.toString()
            setTextColor(setColorEstado(facturaModel.status.toString()))

        }




        //Todo Cambiado por el tema de la foto.


        if (facturaModel.customer?.phototrial != null) {
            binding.itemFacturaIvKiwi.setImageResource(facturaModel.customer.phototrial!!)
        } else {
            binding.itemFacturaIvKiwi.setImageBitmap(facturaModel.customer?.photo)
        }
        //binding.itemFacturaIvKiwi.setImageResource(CustomerProvider.getPhoto(facturaModel.customerId))


        itemView.setOnClickListener { onClickListener?.invoke(facturaModel) }


    }

    fun setColorEstado(status: String): Int {
        if (status.equals("Pendiente")) {
          return Color.parseColor("#FF1100")
        }else if(status.equals("Pagada")) {
          return Color.parseColor("#217C00")
        }else {
          return Color.parseColor("#978303")
        }

    }
}