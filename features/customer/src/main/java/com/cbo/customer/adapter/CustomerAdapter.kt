package com.cbo.customer.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moronlu18.customercreation.R
import com.moronlu18.data.customer.Customer
import com.moronlu18.customercreation.databinding.ItemClienteBinding

class CustomerAdapter(
    private val listener: OnCustomerClick
) : ListAdapter<Customer, CustomerAdapter.CustomerViewHolder>(CUSTOMER_COMPARATOR) {

    //Cuando no hay ninguna posición seleccionada.
    private var selectedPosition = RecyclerView.NO_POSITION

    interface OnCustomerClick {
        fun customerClick(customer: Customer)
        fun customerOnLongClick(view: View, position: Int, customer: Customer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        return CustomerViewHolder(layoutInflater.inflate(R.layout.item_cliente, parent, false))
    }

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    /**
     * Ordena los datos del adaptador por id
     */
    fun sortId() {
        val sortedCustomerList = currentList.sortedBy { it.id }
        submitList(sortedCustomerList)
    }

    /**
     * Ordena los datos del adaptador por nombre y notifica.
     */
    fun sortName() {
        val sortedCustomerList = currentList.sortedBy { it.name.lowercase() }
        submitList(sortedCustomerList)
    }


    /**
     * Desmarca la posición seleccionada y notifica.
     */
    fun clearSelection() {
        selectedPosition = RecyclerView.NO_POSITION
        notifyDataSetChanged()
    }


    inner class CustomerViewHolder(private val view: View) :
        RecyclerView.ViewHolder(view) {

        private val binding = ItemClienteBinding.bind(view)
        fun bind(
            customer: Customer

        ) {
            with(binding) {

                customerListTvName.text = customer.name
                customerListTvEmail.text = customer.email.toString()
                customerListTvCity.text = isValue(customer.city)
                customerListTvPhone.text = isValue(customer.phone)

                customerListTvid.text = customer.id.value.toString()

                if(customer.photo!= null){

                    customerListIvCliente.setImageURI(customer.photo)
                }else{
                    customerListIvCliente.setImageResource(R.drawable.kiwidinero)
                }

                root.setOnClickListener {
                    listener.customerClick(customer)
                    customeItemIvCheck.visibility = View.GONE
                }
                root.setOnLongClickListener {
                    selectedPosition = adapterPosition
                    listener.customerOnLongClick(view, adapterPosition, customer)

                    notifyDataSetChanged()
                    true
                }

                if (adapterPosition == selectedPosition) {
                    customeItemIvCheck.visibility = View.VISIBLE
                } else {
                    customeItemIvCheck.visibility = View.GONE
                }
            }
        }

        /**
         * Función para proporcionar un valor predeterminado ("N/a") si el valor dado es nulo o vacío.
         */
        private fun isValue(value: String?): String {

            return if (value.isNullOrBlank()) {
                "N/a"
            } else {
                value
            }
        }
    }

    companion object {
        //es una clase anonima (??) objeto anonimo (??)
        private val CUSTOMER_COMPARATOR = object : DiffUtil.ItemCallback<Customer>() {
            override fun areItemsTheSame(oldItem: Customer, newItem: Customer): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Customer, newItem: Customer): Boolean {
                return oldItem.name == newItem.name
            }

        }
    }

}