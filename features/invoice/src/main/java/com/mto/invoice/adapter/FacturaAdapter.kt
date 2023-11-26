package com.mto.invoice.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moronlu18.invoicelist.R
import com.moronlu18.accounts.entity.Factura

class FacturaAdapter(
    private val facturaList:List<Factura>,
    private val onClickListener:(Factura) -> Unit,
    private val onClickDelete:(Int) -> Unit
) : RecyclerView.Adapter<FacturaViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacturaViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context);
        return FacturaViewHolder(layoutInflater.inflate(R.layout.item_factura, parent, false))
    }

    override fun onBindViewHolder(holder: FacturaViewHolder, position: Int) {
        val item = facturaList[position]
        holder.render(item, onClickListener, onClickDelete)
    }

    override fun getItemCount(): Int = facturaList.size

}