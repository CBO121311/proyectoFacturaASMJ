package com.cbo.customer.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
//import com.example.pruebasconclientes.R
import com.moronlu18.customercreation.R
//import com.example.pruebasconclientes.data.Clientes
import com.moronlu18.accounts.entity.Customer
import com.moronlu18.customercreation.databinding.ItemClienteBinding

class CustomerAdapter(
    private val listener: OnCustomerClick
) : RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder>() {

    private var dataset = arrayListOf<Customer>()


    interface OnCustomerClick { //Este elemento es público
        fun customerClick(customer: Customer)
        fun customerOnLongClick(customer: Customer)
        fun customerEditClick(position: Int)
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


    fun update(newDataSet: ArrayList<Customer>) {

        dataset = newDataSet
        notifyDataSetChanged()
    }

    fun deleteCustomer(customer: Customer) {
        dataset.remove(customer)
        notifyDataSetChanged()
    }

    fun sortName(){
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
                customerListTvCity.text = customer.city
                customerListTvPhone.text = customer.phone
                customerListTvid.text = customer.id.toString()
                //customerListIvCliente.setImageResource(customer.photo)
                if (customer.phototrial != null) {
                    customerListIvCliente.setImageResource(customer.phototrial!!)
                } else {
                    customerListIvCliente.setImageBitmap(customer.photo)
                }


                root.setOnClickListener {
                    listener.customerClick(customer)
                }
                root.setOnLongClickListener {
                    listener.customerOnLongClick(customer)
                    true
                }

                customerListImgtnEdit.setOnClickListener {
                    listener.customerEditClick(adapterPosition)
                }
            }
        }
    }
}