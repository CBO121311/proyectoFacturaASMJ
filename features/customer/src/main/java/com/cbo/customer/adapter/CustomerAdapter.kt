package com.cbo.customer.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
//import com.example.pruebasconclientes.R
import com.moronlu18.customercreation.R
//import com.example.pruebasconclientes.data.Clientes
import com.moronlu18.accounts.entity.Customer

class CustomerAdapter(
    private var clientesList: List<Customer>,
    private val onClickListener: ((Customer) -> Unit) ? =null,
    private val onClickDelete: ((Int) -> Unit) ? = null
) : RecyclerView.Adapter<CustomerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        return CustomerViewHolder(layoutInflater.inflate(R.layout.item_cliente, parent, false))
    }

    override fun getItemCount(): Int {

        return clientesList.size
    }

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        val item = clientesList[position]
        holder.render(item, onClickListener, onClickDelete)
    }

    /*fun updateList(newList: List<Customer>) {
        clientesList = newList
        notifyDataSetChanged()
    }*/
}