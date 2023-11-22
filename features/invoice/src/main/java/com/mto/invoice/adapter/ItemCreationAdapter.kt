package com.mto.invoice.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moronlu18.invoicelist.R
import com.mto.invoice.data.model.Item


class ItemCreationAdapter(private val itemList: List<Item>, private val onClickListener: (Item) -> Unit): RecyclerView.Adapter<ItemCreationViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemCreationViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ItemCreationViewHolder(layoutInflater.inflate(R.layout.item_itemcreation, parent, false))
    }
    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ItemCreationViewHolder, position: Int) {
        val item = itemList[position]
        holder.render(item, onClickListener)
    }

}