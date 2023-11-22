package com.moronlu18.customer.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.moronlu18.accounts.entity.Clientes
import com.moronlu18.customercreation.databinding.ItemClienteBinding


class ClientesViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemClienteBinding.bind(view)

    fun render(
        clientesModel: Clientes,
        onClickListener: (Clientes) -> Unit,
        onClickDelete: (Int) -> Unit
    ){

        binding.customerListTvName.text = clientesModel.name
        binding.customerListTvEmail.text = clientesModel.email
        binding.customerListTvCity.text= clientesModel.city
        binding.customerListTvPhone.text = clientesModel.phone
        binding.customerListIvCliente.setImageResource(clientesModel.photo)

        itemView.setOnClickListener{onClickListener(clientesModel)}
        binding.customerListImgbtnDelete.setOnClickListener{onClickDelete(adapterPosition)}

    }
}