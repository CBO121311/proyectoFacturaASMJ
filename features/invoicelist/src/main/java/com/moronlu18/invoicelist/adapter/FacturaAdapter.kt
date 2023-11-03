package com.moronlu18.invoicelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moronlu18.invoicelist.adapter.FacturaViewHolder
import com.moronlu18.invoicelist.data.Factura

class FacturaAdapter(
    private val facturaList:List<Factura>,
    private val onClickListener:(Factura) -> Unit
) : RecyclerView.Adapter<FacturaViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacturaViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context);
        return FacturaViewHolder(layoutInflater.inflate(R.layout.item_factura, parent, false))
    }

    override fun onBindViewHolder(holder: FacturaViewHolder, position: Int) {
        val item = facturaList[position]
        holder.render(item, onClickListener)
    }

    override fun getItemCount(): Int = facturaList.size

}