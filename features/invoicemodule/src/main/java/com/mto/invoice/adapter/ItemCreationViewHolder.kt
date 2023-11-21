package com.mto.invoice.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.moronlu18.invoicelist.databinding.ItemItemcreationBinding
import com.mto.invoice.data.model.Item


class ItemCreationViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding = ItemItemcreationBinding.bind(view)

    fun render(itemModel: Item, onClickListener: (Item) -> Unit) {

        binding.invoicecItemItemTvName.text = itemModel.name
        binding.invoicecItemItemTvRate.text = "Unidad: " + itemModel.rate.toString()

        itemView.setOnClickListener { onClickListener(itemModel) }
    }
}