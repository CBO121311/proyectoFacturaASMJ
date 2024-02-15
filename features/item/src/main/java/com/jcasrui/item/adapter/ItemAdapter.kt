package com.jcasrui.item.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moronlu18.accounts.entity.Item
import com.moronlu18.itemcreation.R
import com.moronlu18.itemcreation.databinding.ItemItemBinding

class ItemAdapter(
    private val onClickListener: ((Item) -> Unit)? = null,
    private val onClickEdit: ((Int) -> Unit)? = null,
    private val onClickDelete: ((Int) -> Unit)? = null,
) : ListAdapter<Item, ItemAdapter.ItemViewHolder>(ITEM_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(layoutInflater.inflate(R.layout.item_item, parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onClickListener, onClickEdit, onClickDelete)
    }

    /**
     * Función que ordena el dataset en base a una propiedad personalizada
     */
    fun sort() {
        val sortedItemList = currentList.sortedBy { it.name.lowercase() }
        submitList(sortedItemList)
    }

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemItemBinding.bind(view)
        fun bind(
            itemModel: Item,
            onClickListener: ((Item) -> Unit)? = null,
            onClickEdit: ((Int) -> Unit)? = null,
            onClickDelete: ((Int) -> Unit)? = null,
        ) {
            if (itemModel.photo != null) {
                binding.itemItemCImg.setImageResource(itemModel.photo!!)
            } else {
                binding.itemItemCImg.setImageResource(R.drawable.cart)
            }
            binding.itemItemTvId.text = itemModel.id.value.toString()
            binding.itemItemTvName.text = itemModel.name
            binding.itemItemTvRateContent.text = itemModel.price.toString()

            //binding.itemItemImgBtnEdit.setOnClickListener { onClickEdit?.invoke(adapterPosition) }
            //binding.itemItemImgBtnDelete.setOnClickListener { onClickDelete?.invoke(adapterPosition) }
            itemView.setOnClickListener { onClickListener?.invoke(itemModel) }

            itemView.setOnLongClickListener {
                onClickDelete?.invoke(adapterPosition)
                true
            }
        }
    }

    companion object {
        private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.name == newItem.name
            }
        }
    }
}