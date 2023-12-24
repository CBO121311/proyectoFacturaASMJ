package com.cbo.customer.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.moronlu18.customercreation.R
import com.moronlu18.accounts.entity.Customer
import com.moronlu18.customercreation.databinding.ItemClienteBinding

class CustomerAdapter(
    private val listener: OnCustomerClick
) : RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder>() {

    private var dataset = arrayListOf<Customer>()

    interface OnCustomerClick {
        fun customerClick(position: Int)
        fun customerOnLongClick(customer: Customer)
        fun customerEditClick(position: Int)
        fun showContextMenu(view: View, position: Int, customer: Customer)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        return CustomerViewHolder(layoutInflater.inflate(R.layout.item_cliente, parent, false))
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        val item = dataset[position]
        holder.bind(item)
    }

    /**
     * Actualiza los datos del adaptador con un nuevo conjunto de clientes
     * y notifica se realice un cambio.
     *
     * @param newDataSet Nuevo conjunto de clientes.
     */
    fun update(newDataSet: ArrayList<Customer>) {

        dataset = newDataSet
        notifyDataSetChanged()
    }

    /**
     * Elimina un cliente específico del conjunto de datos del adaptador
     * y notifica que se realice un cambio.
     *
     * @param customer Cliente a eliminar.
     */
    fun deleteCustomer(customer: Customer) {
        dataset.remove(customer)
        notifyDataSetChanged()
    }

    /**
     * Ordena el conjunto de datos del adaptador por nombre y
     * notifica que se realice un cambio.
     */
    fun sortName() {
        dataset.sort()
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

                customerListTvid.text = customer.id.toString()

                if (customer.phototrial != null) {
                    customerListIvCliente.setImageResource(customer.phototrial!!)
                } else {
                    customerListIvCliente.setImageBitmap(customer.photo)
                }

                root.setOnClickListener {
                    listener.customerClick(adapterPosition)
                }
                root.setOnLongClickListener {
                    listener.showContextMenu(view, adapterPosition, customer)
                    //listener.customerOnLongClick(customer)
                    true
                }
            }
        }

        /**
         * Función para proporcionar un valor predeterminado ("N/a") si el valor dado es nulo o vacío.
         *
         * @param value El valor a evaluar.
         * @return El valor original si no es nulo o vacío; de lo contrario, devuelve "N/a".
         */
        private fun isValue(value: String?): String {

            return if (value.isNullOrBlank()) {
                "N/a"
            } else {
                value
            }
        }
    }
}