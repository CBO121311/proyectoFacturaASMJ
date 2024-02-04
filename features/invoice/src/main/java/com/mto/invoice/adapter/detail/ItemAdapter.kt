package com.mto.invoice.adapter.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moronlu18.accounts.entity.Item
import com.moronlu18.invoicelist.R
import com.moronlu18.invoicelist.databinding.ItemItemdetailBinding


class ItemAdapter(private val itemList: List<Item>, private val onClickListener: (Item) -> Unit): RecyclerView.Adapter<ItemAdapter.ItemViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(layoutInflater.inflate(R.layout.item_itemdetail, parent, false))
    }
    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]
        holder.render(item, onClickListener)
    }

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val binding = ItemItemdetailBinding.bind(view)

        fun render(itemModel: Item, onClickListener: (Item) -> Unit) {
            binding.invoicedItemItemTvName.text = truncarString(itemModel.name)
            binding.invoicedItemItemTvDescription.text = itemModel.description
            binding.invoicedItemItemTvTypeContent.text = itemModel.type.name
            binding.invoicedItemItemTvRateContent.text = itemModel.price.toString()
            //ToDo fallo por la foto del item
            //binding.invoicedItemItemIvItem.setImageResource(itemModel.photo!!)
            itemView.setOnClickListener { onClickListener(itemModel) }
        }

        fun truncarString(str: String): String {
            return if (str.length > 14) {
                str.substring(0, 12) + "..."
            } else {
                str
            }
        }
    }

}