package com.jcasrui.itemcreation.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.moronlu18.accounts.entity.Item
import com.moronlu18.itemcreation.databinding.ItemItemBinding

class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemItemBinding.bind(view)
    fun render(
        itemModel: Item,
        onClickListener: (Item) -> Unit,
    ) {
        binding.itemItemTvType.text = "Tipo:"
        binding.itemItemTvRate.text = "Tasa:"
        //binding.itemItemCbTaxable.text = "Impuestos:"

        binding.itemItemIvImg.setImageResource(itemModel.image)
        binding.itemItemTvName.text = itemModel.name
        binding.itemItemTvDescription.text = itemModel.description
        binding.itemItemTvTypeContent.text = itemModel.type
        binding.itemItemTvRateContent.text = itemModel.rate.toString()
        //binding.itemItemCbTaxable.text = "Impuestos: ${itemModel.taxable}"

        itemView.setOnClickListener { onClickListener(itemModel) }
    }
}