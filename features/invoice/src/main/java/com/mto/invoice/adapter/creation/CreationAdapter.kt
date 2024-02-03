package com.mto.invoice.adapter.creation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moronlu18.accounts.entity.Item
import com.moronlu18.invoicelist.R
import com.moronlu18.invoicelist.databinding.ItemItemcreationBinding

class CreationAdapter(
    private val onClickListener: (Item) -> Unit
) : ListAdapter<Item, CreationAdapter.CreationItemViewHolder>(ITEM_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreationItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context);
        return CreationItemViewHolder(layoutInflater.inflate(R.layout.item_itemcreation, parent, false))
    }

    override fun onBindViewHolder(holder: CreationItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.render(item, onClickListener)
    }

    override fun getItemCount(): Int = currentList.size

    inner class CreationItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemItemcreationBinding.bind(view)
        fun render(itemModel: Item, onClickListener: (Item) -> Unit) {

            binding.invoicecItemItemTvName.text = itemModel.name
            binding.invoicecItemItemTvRate.text = "Unidad: " + itemModel.price.toString()
            itemView.setOnClickListener { onClickListener(itemModel) }
        }
    }

    companion object{
        private val ITEM_COMPARATOR = object:DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }

}