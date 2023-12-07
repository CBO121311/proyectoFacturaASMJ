package com.jcasrui.item.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moronlu18.accounts.entity.Item
import com.moronlu18.itemcreation.R

class ItemAdapter(
    private val itemList: List<Item>,
    private val onClickListener: (Item) -> Unit,
    private val onClickDelete: (Int) -> Unit,
) : RecyclerView.Adapter<ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // Encargado de coger los atributos y pintarlos
        val layoutInflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(layoutInflater.inflate(R.layout.item_item, parent, false))
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]
        holder.render(item, onClickListener, onClickDelete)
    }
}