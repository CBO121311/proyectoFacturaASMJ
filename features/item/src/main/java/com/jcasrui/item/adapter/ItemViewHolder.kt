package com.jcasrui.item.adapter


import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.moronlu18.accounts.entity.Item
import com.moronlu18.itemcreation.databinding.ItemItemBinding

class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemItemBinding.bind(view)
    fun render(
        itemModel: Item,
        onClickListener: (Item) -> Unit,
        onClickDelete: (Int) -> Unit,
    ) {
        binding.itemItemCImg.setImageResource(itemModel.image)
        binding.itemItemTvId.text = itemModel.id.toString()
        binding.itemItemTvName.text = itemModel.name
        binding.itemItemTvRateContent.text = itemModel.rate.toString()

        itemView.setOnClickListener { onClickListener(itemModel) }
        binding.itemItemImgBtnDelete.setOnClickListener { onClickDelete(adapterPosition) }
    }
}