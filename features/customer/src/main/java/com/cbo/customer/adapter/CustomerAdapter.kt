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
    private var clientesList: List<Customer>,
    private val onClickListener: ((Customer) -> Unit)? = null,
    private val onClickDelete: ((Int) -> Unit)? = null,
    private val onClickEdit: ((Int) -> Unit)? = null
) : RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        return CustomerViewHolder(layoutInflater.inflate(R.layout.item_cliente, parent, false))
    }

    /*
    *    val layoutInflater = LayoutInflater.from(parent.context)
            return CustomerViewHolder(layoutInflater.inflate(R.layout.item_cliente, parent, false))

    * */
    override fun getItemCount(): Int {

        return clientesList.size
    }

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        val item = clientesList[position]
        holder.bind(item, onClickListener, onClickDelete, onClickEdit)
    }

    inner class CustomerViewHolder(private val view: View) :
        RecyclerView.ViewHolder(view) {

        private val binding = ItemClienteBinding.bind(view)
        fun bind(
            clientesModel: Customer,
            onClickListener: ((Customer) -> Unit)? = null,
            onClickDelete: ((Int) -> Unit)? = null,
            onClickEdit: ((Int) -> Unit)?

        ) {

            with(binding){

                customerListTvName.text = clientesModel.name
                customerListTvEmail.text = clientesModel.email.toString()
                customerListTvCity.text = clientesModel.city
                customerListTvPhone.text = clientesModel.phone
                customerListTvid.text = clientesModel.id.toString()
                customerListIvCliente.setImageResource(clientesModel.photo)

                customerListImgtnEdit.setOnClickListener {
                    onClickEdit?.invoke(adapterPosition)
                }
                customerListImgbtnDelete.setOnClickListener {
                    onClickDelete?.invoke(
                        adapterPosition
                    )
                }
            }
            itemView.setOnClickListener { onClickListener?.invoke(clientesModel) }


        }
    }
}