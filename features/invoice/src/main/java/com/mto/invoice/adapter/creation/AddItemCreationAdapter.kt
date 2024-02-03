package com.mto.invoice.adapter.creation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moronlu18.accounts.entity.Item
import com.moronlu18.invoicelist.R
import com.moronlu18.invoicelist.databinding.ItemItemcreationBinding


class AddItemCreationAdapter (private val itemList: MutableList<Item>): RecyclerView.Adapter<AddItemCreationAdapter.AddItemCreationViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddItemCreationViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return AddItemCreationViewHolder(layoutInflater.inflate(R.layout.item_itemcreation, parent, false))
    }
    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: AddItemCreationViewHolder, position: Int) {
        val item = itemList[position]
        holder.render(item)
    }

    inner class AddItemCreationViewHolder (view: View) : RecyclerView.ViewHolder(view)  {
        val binding = ItemItemcreationBinding.bind(view)
        fun render(itemModel: Item) {
            binding.invoicecItemItemTvName.text = itemModel.name
            binding.invoicecItemItemTvRate.text = "P/U: " + itemModel.price.toString()
            binding.invoiceItemItemTvUnidades.text ="1"

        }
    }
}