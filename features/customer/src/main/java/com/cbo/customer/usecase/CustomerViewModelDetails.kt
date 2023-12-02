package com.cbo.customer.usecase

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CustomerViewModelDetails : ViewModel() {

     var idCustomer = MutableLiveData<String>()
     var nameCustomer = MutableLiveData<String>()
     var emailCustomer = MutableLiveData<String>()
     var phoneCustomer = MutableLiveData<String>()
     var addressCustomer = MutableLiveData<String>()
     var cityCustomer = MutableLiveData<String>()


}