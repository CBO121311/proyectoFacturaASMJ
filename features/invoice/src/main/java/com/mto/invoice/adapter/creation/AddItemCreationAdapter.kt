package com.mto.invoice.adapter.creation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moronlu18.accounts.entity.Item
import com.moronlu18.invoicelist.R


class AddItemCreationAdapter (private val itemList: MutableList<Item>): RecyclerView.Adapter<AddItemCreationViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddItemCreationViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return AddItemCreationViewHolder(layoutInflater.inflate(R.layout.item_itemcreation, parent, false))
    }
    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: AddItemCreationViewHolder, position: Int) {
        val item = itemList[position]
        holder.render(item)
    }
}