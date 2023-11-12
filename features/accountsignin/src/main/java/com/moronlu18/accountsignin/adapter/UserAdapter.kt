package com.moronlu18.accountsignin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moronlu18.accountsignin.data.model.User
import com.moronlu18.accountsignin.databinding.FragmentLayoutUserItemBinding


class UserAdapter(private val dataset: MutableList<User>, private val context: Context) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context) //Esto es un manager.

        return UserViewHolder(FragmentLayoutUserItemBinding.inflate(layoutInflater, parent, false))
    }


    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = dataset.get(position)
        holder.bind(item, context)
    }

    /**
     * Función que devuelve el número de elementos y por tanto se llamará al método onCreateViewHolder,
     * tantas veces como item se visualicen en el RecyclerView
     * Item se visualen en el RecyclerView
     */
    override fun getItemCount(): Int {
        return dataset.size
    }


    /**
     * La clase viewHolde contiene todos los elementos de view o del layout XML que se ha inflado.
     */
    class UserViewHolder(private val binding: FragmentLayoutUserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: User, context: Context) {
            binding.imgUser.text = item.name.substring(0, 1).uppercase()
            binding.tvName.text = item.name
            binding.tvEmail.text = item.email
        }
    }
}