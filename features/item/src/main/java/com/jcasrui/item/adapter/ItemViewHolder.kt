package com.jcasrui.item.adapter


import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.moronlu18.accounts.entity.Item
import com.moronlu18.itemcreation.databinding.ItemItemBinding

class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemItemBinding.bind(view)
    fun bind(
        itemModel: Item,
        onClickListener: ((Item) -> Unit)? = null,
        onClickEdit: ((Int) -> Unit)? = null,
        onClickDelete: ((Int) -> Unit)? = null,
    ) {
        binding.itemItemCImg.setImageResource(itemModel.image)
        binding.itemItemTvId.text = itemModel.id.toString()
        binding.itemItemTvName.text = itemModel.name
        binding.itemItemTvRateContent.text = itemModel.rate.toString()

        binding.itemItemImgBtnEdit.setOnClickListener { onClickEdit?.invoke(adapterPosition) }
        binding.itemItemImgBtnDelete.setOnClickListener { onClickDelete?.invoke(adapterPosition) }
        itemView.setOnClickListener { onClickListener?.invoke(itemModel) }
    }
}