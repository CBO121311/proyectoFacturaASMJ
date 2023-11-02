package com.cbo.customerlist.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.cbo.customerlist.data.Clientes
import com.moronlu18.customerlist.databinding.ItemClienteBinding

//import com.example.pruebasconclientes.databinding.ItemClienteBinding

class ClientesViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemClienteBinding.bind(view)

    fun render(clientesModel: Clientes, onClickListener: (Clientes) -> Unit){

        binding.customerListTvNombre.text = clientesModel.name
        binding.customerListTvCorreo.text = clientesModel.email
        binding.customerListTvDireccion.text = clientesModel.adress
        binding.customerListTvCiudad.text= clientesModel.city
        binding.customerListTvTelefono.text = clientesModel.phone

        itemView.setOnClickListener{onClickListener(clientesModel)}

    }
}