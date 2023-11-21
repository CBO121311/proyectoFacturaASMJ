package com.mto.invoice.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.moronlu18.invoicelist.databinding.ItemItemdetailBinding
import com.mto.invoice.data.model.Item


class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding = ItemItemdetailBinding.bind(view)

    fun render(itemModel: Item, onClickListener: (Item) -> Unit) {

        binding.invoicedItemItemTvName.text = itemModel.name
        binding.invoicedItemItemTvDescription.text = itemModel.description
        binding.invoicedItemItemTvTypeContent.text = itemModel.type
        binding.invoicedItemItemTvRateContent.text = itemModel.rate.toString()

        itemView.setOnClickListener { onClickListener(itemModel) }
    }
}