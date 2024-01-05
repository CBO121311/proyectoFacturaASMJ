package com.mto.invoice.adapter.detail

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.moronlu18.accounts.entity.Item
import com.moronlu18.invoicelist.databinding.ItemItemdetailBinding



class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding = ItemItemdetailBinding.bind(view)

    fun render(itemModel: Item, onClickListener: (Item) -> Unit) {

        binding.invoicedItemItemTvName.text = itemModel.name
        binding.invoicedItemItemTvDescription.text = itemModel.description
        binding.invoicedItemItemTvTypeContent.text = itemModel.type.name
        binding.invoicedItemItemTvRateContent.text = itemModel.rate.toString()
        binding.invoicedItemItemIvItem.setImageResource(itemModel.image)
        itemView.setOnClickListener { onClickListener(itemModel) }
    }
}