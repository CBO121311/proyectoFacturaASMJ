package com.cbo.customerlist.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
//import com.example.pruebasconclientes.R
import com.moronlu18.customerlist.R
//import com.example.pruebasconclientes.data.Clientes
import com.cbo.customerlist.data.model.Clientes

class ClientesAdapter(
    private val clientesList: List<Clientes>,
    private val onClickListener: (Clientes) -> Unit,
    private val onClickDelete:(Int) -> Unit
) : RecyclerView.Adapter<ClientesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientesViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        return ClientesViewHolder(layoutInflater.inflate(R.layout.item_cliente, parent, false))
    }

    override fun getItemCount(): Int {

        return clientesList.size
    }

    override fun onBindViewHolder(holder: ClientesViewHolder, position: Int) {
        val item = clientesList[position]
        holder.render(item, onClickListener, onClickDelete)


    }
}