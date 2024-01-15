package com.mto.invoice.adapter.list

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.moronlu18.accounts.entity.Invoice

import com.moronlu18.invoicelist.databinding.ItemFacturaBinding

class FacturaViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemFacturaBinding.bind(view)

    fun render(
        invoiceModel: Invoice,
        onClickListener: (Invoice) -> Unit,

        ) {

        binding.itemFacturaIvtTotal.text = "Total: ${invoiceModel.number}"
        binding.itemFacturaTvId.text = invoiceModel.id.toString()
        binding.itemFacturaTvCliente.text = invoiceModel.customer.name
        with(binding.itemFacturaEstado) {
            text = primerCaracterMayuscula(invoiceModel.status.toString())
            setTextColor(setColorEstado(invoiceModel.status.toString()))

        }


        //Todo Cambiado por el tema de la foto.


        if (invoiceModel.customer?.phototrial != null) {
            binding.itemFacturaIvKiwi.setImageResource(invoiceModel.customer.phototrial!!)
        } else {
            binding.itemFacturaIvKiwi.setImageBitmap(invoiceModel.customer?.photo)
        }
        //binding.itemFacturaIvKiwi.setImageResource(CustomerProvider.getPhoto(facturaModel.customerId))

        itemView.setOnClickListener { onClickListener?.invoke(invoiceModel) }


    }

    /**
     * Función que devuelve un entero que representa un color,
     * en función del estado de la factura
     */
    fun setColorEstado(status: String): Int {
        return if (status.equals("PENDIENTE")) {
            Color.parseColor("#FF1100")
        } else if (status.equals("PAGADA")) {
            Color.parseColor("#217C00")
        } else {
            Color.parseColor("#978303")
        }

    }

    /**
     * Función que recibe un string y lo devuelve con todas las letras, menos la primera,
     * a minúscula
     */
    fun primerCaracterMayuscula(texto: String): String {
        // Obtener el primer carácter en mayúscula y concatenarlo con el resto del texto
        return texto.substring(0, 1) + texto.substring(1).lowercase()

    }
}