package com.cbo.customerlist.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.cbo.customerlist.data.model.Clientes
import com.moronlu18.customerlist.databinding.ItemClienteBinding


class ClientesViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemClienteBinding.bind(view)

    fun render(clientesModel: Clientes, onClickListener: (Clientes) -> Unit){

        binding.customerListTvName.text = clientesModel.name
        binding.customerListTvEmail.text = clientesModel.email
        binding.customerListTvAddress.text = clientesModel.adress
        binding.customerListTvCity.text= clientesModel.city
        binding.customerListTvPhone.text = clientesModel.phone

        itemView.setOnClickListener{onClickListener(clientesModel)}

    }
}