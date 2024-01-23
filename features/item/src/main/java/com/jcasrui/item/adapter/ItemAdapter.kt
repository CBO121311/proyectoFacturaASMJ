package com.jcasrui.item.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moronlu18.accounts.entity.Item
import com.moronlu18.itemcreation.R

class ItemAdapter(
    private val onClickListener: ((Item) -> Unit)? = null,
    private val onClickEdit: ((Int) -> Unit)? = null,
    private val onClickDelete: ((Int) -> Unit)? = null,
) : RecyclerView.Adapter<ItemViewHolder>() {

    private var dataset = arrayListOf<Item>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(layoutInflater.inflate(R.layout.item_item, parent, false))
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.bind(item, onClickListener, onClickEdit, onClickDelete)
    }

    /**
     * Actuzaliza los datos del adapter
     */
    fun update(newDataSet: ArrayList<Item>) {
        dataset = newDataSet
        notifyDataSetChanged()
    }

    /**
     * Función que ordena el dataset en base a una propiedad personalizada
     */
    fun sort() {
        //dataset.sortBy { it.name }
        dataset.sort()
        notifyDataSetChanged()
    }
}