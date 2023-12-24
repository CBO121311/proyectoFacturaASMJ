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

    //Cuando no hay ninguna posición seleccionada.
    private var selectedPosition = RecyclerView.NO_POSITION

    interface OnCustomerClick {
        fun customerClick(position: Int)
        fun customerOnLongClick(view: View, position: Int, customer: Customer)
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
     * Actualiza los datos del adaptador con nuevo datos de clientes y notifica.
     *
     */
    fun update(newDataSet: ArrayList<Customer>) {

        dataset = newDataSet
        notifyDataSetChanged()
    }

    /**
     * Elimina un cliente específico de los datos del adaptador y notifica.
     *
     */
    fun deleteCustomer(customer: Customer) {
        dataset.remove(customer)
        notifyDataSetChanged()
    }

    /**
     * Ordena los datos del adaptador por nombre y notifica.
     */
    fun sortName() {
        dataset.sort()
        notifyDataSetChanged()
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

                customerListTvid.text = customer.id.toString()

                if (customer.phototrial != null) {
                    customerListIvCliente.setImageResource(customer.phototrial!!)
                } else {
                    customerListIvCliente.setImageBitmap(customer.photo)
                }

                root.setOnClickListener {
                    listener.customerClick(adapterPosition)
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
         *
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