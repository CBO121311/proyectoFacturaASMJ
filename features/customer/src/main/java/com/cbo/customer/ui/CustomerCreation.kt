package com.cbo.customer.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.cbo.customer.usecase.CustomerViewModel
import com.google.android.material.textfield.TextInputLayout
import com.moronlu18.accounts.entity.Customer
import com.moronlu18.accounts.entity.Email
import com.moronlu18.accounts.repository.CustomerProvider
import com.moronlu18.customercreation.R
import com.moronlu18.customercreation.databinding.FragmentCustomerCreationBinding


class CustomerCreation : Fragment() {
    private var customerList = CustomerProvider.CustomerdataSet
    private var _binding: FragmentCustomerCreationBinding? = null
    private val binding get() = _binding!!
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private val viewModel: CustomerViewModel by viewModels()
    private var isEditMode = false
    private var editCustomerPos = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCustomerCreationBinding.inflate(inflater, container, false)

        binding.viewmodelcustomercreation = this.viewModel
        binding.lifecycleOwner = this


        parentFragmentManager.setFragmentResultListener(
            "customkey", this, FragmentResultListener { _, result ->
                var posCustomer: Int = result.getInt("position")
                val customerEdit = customerList[posCustomer]

                binding.customerCreationTietEmailCustomer.setText(customerEdit.email.toString())
                binding.customerCreationTietNameCustomer.setText(customerEdit.name)

                isAvailable(binding.customerCreationTietAddress, customerEdit.address)

                isAvailable(binding.customerCreationTietAddress, customerEdit.address)
                isAvailable(binding.customerCreationTietPhone, customerEdit.phone)
                isAvailable(binding.customerCreationTietCity, customerEdit.city)
                binding.customerCreationImgcAvatar.setImageResource(customerEdit.photo)
                isEditMode = true
                editCustomerPos = posCustomer
            }
        )




        return binding.root
    }

    private fun isAvailable(field: TextView, value: String) {
        field.text = if (value != "No disponible") value else null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //Prueba de galería
        binding.customerCreationImgbtnCustomer.setOnClickListener {
            openGallery()
        }

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data: Intent? = it.data
                val imageUri = data?.data
                binding.customerCreationImgcAvatar.setImageURI(imageUri)
            }
        }

        viewModel.getState().observe(viewLifecycleOwner, Observer {
            when (it) {

                CustomerState.NameIsMandatory -> setNameCustomerEmptyError()
                CustomerState.EmailEmptyError -> setEmailEmptyError()
                CustomerState.InvalidEmailFormat -> setEmailFormatError()
                CustomerState.OnSuccess -> onSuccessCreate()
                else -> {}//onSuccessCreate()
            }
        })
        binding.customerCreationTietNameCustomer.addTextChangedListener(CcWatcher(binding.customerCreationTilNameCustomer))
        binding.customerCreationTietEmailCustomer.addTextChangedListener(CcWatcher(binding.customerCreationTilCustomerEmail))
    }


    /**
     * Función cuando crea con éxito un cliente.
     */
    private fun onSuccessCreate() {
        val name = binding.customerCreationTietNameCustomer.text.toString()
        val email = binding.customerCreationTietEmailCustomer.text.toString()
        val phone = binding.customerCreationTietPhone.text.toString()
        val city = binding.customerCreationTietCity.text.toString()
        val address = binding.customerCreationTietAddress.text.toString()
        //val photo = binding.customerCreationImgcAvatar

        if (isEditMode) {
            val editCustomer = customerList[editCustomerPos]
            val updatedCustomer = Customer(
                id = editCustomer.id,
                name = name,
                email = Email(email),
                phone = phone.ifEmpty { "No disponible" },
                city = city.ifEmpty { "No disponible" },
                address = address.ifEmpty { "No disponible" },
                photo = R.drawable.kiwidiner_background
            )

        } else {
            val customer = Customer(
                id = customerList.size + 1,
                name = name,
                email = Email(email),
                phone = phone.ifEmpty { "No disponible" },
                city = city.ifEmpty { "No disponible" },
                address = address.ifEmpty { "No disponible" },
                photo = R.drawable.kiwidiner_background
            )
            customerList.add(customer)
        }

        findNavController().popBackStack()
    }


    /**
     * TextWatcher para anular el mensaje de error
     */
    open inner class CcWatcher(private val til: TextInputLayout) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            til.error = null
        }
    }


    /**
     * Función que muestra el error de Nombre vacío
     */
    private fun setNameCustomerEmptyError() {
        binding.customerCreationTilNameCustomer.error = "Nombre vacío"
        binding.customerCreationTilNameCustomer.requestFocus()
    }

    /**
     * Función que muestra el error de Email vacío
     */
    private fun setEmailEmptyError() {
        binding.customerCreationTilCustomerEmail.error = "Email vacío"
        binding.customerCreationTilCustomerEmail.requestFocus()
    }

    /**
     * Función que muestra el error de formato de Email
     */
    private fun setEmailFormatError() {
        binding.customerCreationTilCustomerEmail.error = "Formato de Email erróneo"
        binding.customerCreationTilCustomerEmail.requestFocus()
    }

    /**
     * Abre la galería.
     */
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        launcher.launch(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}