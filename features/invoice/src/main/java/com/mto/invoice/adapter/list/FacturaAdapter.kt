package com.mto.invoice.adapter.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moronlu18.data.customer.Customer
import com.moronlu18.invoicelist.R
import com.moronlu18.data.invoice.Invoice
import com.moronlu18.repository.CustomerProvider

class FacturaAdapter(
    private val onClickListener:(Invoice) -> Unit
) : RecyclerView.Adapter<FacturaViewHolder>(){

    private var invoiceList = arrayListOf<Invoice>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacturaViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context);
        return FacturaViewHolder(layoutInflater.inflate(R.layout.item_factura, parent, false))
    }

    override fun onBindViewHolder(holder: FacturaViewHolder, position: Int) {
        val item = invoiceList[position]
        holder.render(item, onClickListener)
    }

    fun update(newDataSet:ArrayList<Invoice>){
        invoiceList = newDataSet
        notifyDataSetChanged()
    }



    //Todo por el tema del sort del customerId
    /*fun sort() {


        invoiceList.sortBy { it.customerId.name }
        notifyDataSetChanged()
    }*/

    fun sort() {
        invoiceList.sortBy { getCustomer(it.customerId.value) }
        notifyDataSetChanged()
    }

    private fun getCustomer(customerId: Int): Customer {
        return CustomerProvider.getCustomerPos(customerId)
    }




    override fun getItemCount(): Int = invoiceList.size

}