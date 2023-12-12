package com.mto.invoice.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moronlu18.invoicelist.R
import com.moronlu18.accounts.entity.Factura

class FacturaAdapter(
    private val onClickListener:(Factura) -> Unit,
    private val onClickDelete:((Int) -> Unit)? = null
) : RecyclerView.Adapter<FacturaViewHolder>(){

    private var facturaList = arrayListOf<Factura>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacturaViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context);
        return FacturaViewHolder(layoutInflater.inflate(R.layout.item_factura, parent, false))
    }

    override fun onBindViewHolder(holder: FacturaViewHolder, position: Int) {
        val item = facturaList[position]
        holder.render(item, onClickListener, onClickDelete)
    }

    fun update(newDataSet:ArrayList<Factura>){
        facturaList = newDataSet
        notifyDataSetChanged()
    }

    fun deleteInvoice(position: Int) {
        facturaList.removeAt(position)
        notifyItemRemoved(position)
    }
    fun sort() {
        facturaList.sortBy { it.customerId }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = facturaList.size

}