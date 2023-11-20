package com.mto.invoicecreation.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.moronlu18.invoicecreation.databinding.ItemItemBinding

import com.mto.invoicecreation.data.Item


class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding = ItemItemBinding.bind(view)

    fun render(itemModel: Item, onClickListener: (Item) -> Unit) {

        binding.invoicecItemItemTvName.text = itemModel.name
        binding.invoicecItemItemTvRate.text = "Unidad: " + itemModel.rate.toString()

        itemView.setOnClickListener { onClickListener(itemModel) }
    }
}