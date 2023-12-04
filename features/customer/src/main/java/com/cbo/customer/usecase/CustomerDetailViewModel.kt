package com.cbo.customer.usecase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cbo.customer.ui.CustomerDetailState
import com.moronlu18.accounts.entity.Customer
import com.moronlu18.accounts.repository.CustomerProvider

class CustomerDetailViewModel : ViewModel() {

    private var state = MutableLiveData<CustomerDetailState>()
    var idCustomer = MutableLiveData<String>()
    var nameCustomer = MutableLiveData<String>()
    var emailCustomer = MutableLiveData<String>()
    var phoneCustomer = MutableLiveData<String>()
    var addressCustomer = MutableLiveData<String>()
    var cityCustomer = MutableLiveData<String>()
    private val repository = CustomerProvider

    fun getState(): LiveData<CustomerDetailState> {
        return state
    }

    fun onSuccess() {
        state.value = CustomerDetailState.OnSuccess
    }
    private fun getPositionById(customer: Customer): Int {

        return repository.getPosByCustomer(customer)
    }

    fun deleteCustomer(customer: Customer) {
        val position = getPositionById(customer)
        if (position != -1) {
            repository.deleteCustomer(position)
        }
    }

    fun isDeleteSafe(customer: Customer): Boolean {
        val isSafe = repository.isCustomerSafeDelete(customer.id)
        if (isSafe) {
            state.value = CustomerDetailState.ReferencedCustomer
        }
        return isSafe
    }
}