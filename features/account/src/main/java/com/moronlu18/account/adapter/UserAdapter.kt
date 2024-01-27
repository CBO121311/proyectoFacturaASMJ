package com.moronlu18.account.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moronlu18.data.account.User
import com.moronlu18.accountsignin.databinding.FragmentLayoutUserItemBinding

class UserAdapter(
    private val listener: OnUserClick,
    private val onItemClick: (user: User) -> Unit,
    //private val onItemDelete: (user:User) -> Unit
) : ListAdapter<User, UserAdapter.UserViewHolder>(USER_COMPARATOR) { //del tipo que hemos establecido.
   // RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    //private var dataset = arrayListOf<User>() ya que tiene una lista interna

    /**
     * Esta interfaz es el contrato entre el Adapter y el fragmento que lo contiene.
     */
    interface OnUserClick { //Este elemento es público
        fun userClick(user: User) //Pulsación corta
        fun userOnLongClick(user: User) //Larga

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context) //Esto es un manager.

        return UserViewHolder(FragmentLayoutUserItemBinding.inflate(layoutInflater, parent, false))
    }


    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        //Ya no tengo un dataset, sino lista interna
        //Se accede a un elemento de la lista interna de ListAdapter mediante el método
        //getItem(position)
        val item = getItem(position)
        holder.bind(item)
    }

    /*
    //ListAdapter hereda de recyclerView
    override fun getItemCount(): Int {
        return dataset.size
    }
    fun update(newDataSet: ArrayList<User>) {

        dataset = newDataSet
        notifyDataSetChanged()
    }*/

    /**
     * Función que ordena el data en base a una propiedad personalizada
     */
    fun sort() {
        //Orden personalizada se establece utilizando currentList.
        currentList.sortBy { it.email }

        notifyDataSetChanged()
    }

    fun removeUser(user: User) {
        currentList.remove(user)
        notifyDataSetChanged()
    }


    /**
     * La clase viewHolde contiene todos los elementos de view o del layout XML que se ha inflado.
     */
    inner class UserViewHolder(private val binding: FragmentLayoutUserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: User) {

            with(binding) {
                imgUser.text = item.name.substring(0, 1).uppercase()
                tvName.text = item.name
                tvEmail.text = item.email.toString()
                root.setOnClickListener { _ ->
                    onItemClick(item)
                }
                root.setOnLongClickListener { _ ->
                    listener.userOnLongClick(item)

                    //listener.userOnLongClick(item)
                    //onItemDelete(item)
                    true
                }
            }
        }
    }


    //Hacer comparator.
    //Esto se ejecuta cuando se hace submit y le paso los nuevos datos del flujo.
    companion object{
        //es una clase anonima (??) objeto anonimo (??)
        private val USER_COMPARATOR = object:DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.name == newItem.name
            }


        }
    }
}