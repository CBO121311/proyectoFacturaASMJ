package com.cbo.customer.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.moronlu18.accounts.entity.Customer
import com.moronlu18.customercreation.databinding.ItemClienteBinding


class CustomerViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemClienteBinding.bind(view)

    fun render(
        clientesModel: Customer,
        onClickListener: ((Customer) -> Unit)? = null,
        onClickDelete: ((Int) -> Unit)? = null
    ){

        binding.customerListTvName.text = clientesModel.name
        binding.customerListTvEmail.text = clientesModel.email
        binding.customerListTvCity.text= clientesModel.city
        binding.customerListTvPhone.text = clientesModel.phone
        binding.customerListIvCliente.setImageResource(clientesModel.photo)

        itemView.setOnClickListener{onClickListener?.invoke(clientesModel)}
        binding.customerListImgbtnDelete.setOnClickListener{onClickDelete?.invoke(adapterPosition)}
    }
}