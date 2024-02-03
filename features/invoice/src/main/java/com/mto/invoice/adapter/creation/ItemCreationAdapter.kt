package com.mto.invoice.adapter.creation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moronlu18.accounts.entity.Item
import com.moronlu18.invoicelist.R
import com.moronlu18.invoicelist.databinding.ItemItemcreationBinding


class ItemCreationAdapter(private val itemList: List<Item>, private val onClickListener: (Item) -> Unit): RecyclerView.Adapter<ItemCreationAdapter.ItemCreationViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCreationViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ItemCreationViewHolder(layoutInflater.inflate(R.layout.item_itemcreation, parent, false))
    }
    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ItemCreationViewHolder, position: Int) {
        val item = itemList[position]
        holder.render(item, onClickListener)
    }

    inner class ItemCreationViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val binding = ItemItemcreationBinding.bind(view)

        fun render(itemModel: Item, onClickListener: (Item) -> Unit) {

            binding.invoicecItemItemTvName.text = itemModel.name
            binding.invoicecItemItemTvRate.text = "Unidad: " + itemModel.price.toString()

            itemView.setOnClickListener { onClickListener(itemModel) }
        }
    }

}