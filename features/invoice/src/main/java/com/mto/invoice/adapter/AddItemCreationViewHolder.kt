package com.mto.invoice.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.moronlu18.accounts.entity.Item
import com.moronlu18.invoicelist.databinding.ItemItemcreationBinding

class AddItemCreationViewHolder (view: View, acu:Int) : RecyclerView.ViewHolder(view)  {
    val binding = ItemItemcreationBinding.bind(view)
    var acumulador:Int = acu
    fun render(itemModel: Item) {

        binding.invoicecItemItemTvName.text = itemModel.name
        binding.invoicecItemItemTvRate.text = "P/U: " + itemModel.rate.toString()
        binding.invoiceItemItemTvUnidades.text = acumulador.toString()

    }
}