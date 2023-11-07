package com.jcasrui.itemlist.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.jcasrui.itemlist.data.Item
import com.moronlu18.itemlist.databinding.ItemItemBinding

class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding = ItemItemBinding.bind(view)

    fun render(itemModel: Item, onClickListener: (Item) -> Unit) {
        binding.itemItemTvType.text = "Tipo: "
        binding.itemItemTvRate.text = "Tasa: "
        binding.itemItemTvTaxable.text = "Impuestos: "

        binding.itemItemTvName.text = itemModel.name
        binding.itemItemTvDescription.text = itemModel.description
        binding.itemItemTvTypeContent.text = itemModel.type
        binding.itemItemTvRateContent.text = itemModel.rate.toString()
        binding.itemItemTvTaxableContent.text = itemModel.taxable.toString()

        itemView.setOnClickListener { onClickListener(itemModel) }
    }
}