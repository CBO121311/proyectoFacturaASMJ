package com.mto.invoice.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moronlu18.accounts.entity.Item
import com.moronlu18.invoicelist.R

class AddItemCreationAdapter (private val itemList: MutableList<Item>, acumulador:Int): RecyclerView.Adapter<AddItemCreationViewHolder>() {
    var acumulador:Int = acumulador
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddItemCreationViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return AddItemCreationViewHolder(layoutInflater.inflate(R.layout.item_itemcreation, parent, false), acumulador)
    }
    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: AddItemCreationViewHolder, position: Int) {
        val item = itemList[position]
        holder.render(item)
    }
}