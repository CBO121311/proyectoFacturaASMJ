package com.mto.invoicedetail.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.moronlu18.invoicedetail.databinding.ItemItemBinding
import com.mto.invoicedetail.data.Item

class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding = ItemItemBinding.bind(view)

    fun render(itemModel: Item, onClickListener: (Item) -> Unit) {

        binding.itemItemTvName.text = itemModel.name
        binding.itemItemTvDescription.text = itemModel.description
        binding.itemItemTvTypeContent.text = itemModel.type
        binding.itemItemTvRateContent.text = itemModel.rate.toString()

        itemView.setOnClickListener { onClickListener(itemModel) }
    }
}