package com.jcasrui.itemlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jcasrui.itemlist.data.Item
import com.moronlu18.itemlist.R

class ItemAdapter(private val itemList: List<Item>, private val onClickListener: (Item) -> Unit): RecyclerView.Adapter<ItemViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // encargado de coger los atributos y pintarlos
        val layoutInflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(layoutInflater.inflate(R.layout.item_item, parent, false))
    }

    // Devolver el tamaño del listado
    /*override fun getItemCount(): Int {
        return itemList.size
    }*/
    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]
        holder.render(item, onClickListener)
    }

}