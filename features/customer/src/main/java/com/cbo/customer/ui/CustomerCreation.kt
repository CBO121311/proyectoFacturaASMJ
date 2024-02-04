package com.cbo.customer.ui



import android.net.Uri
import android.os.Bundle

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.cbo.customer.usecase.CustomerViewModel
import com.google.android.material.textfield.TextInputLayout
import com.moronlu18.data.customer.Customer
import com.moronlu18.data.account.Email
import com.moronlu18.customercreation.R
import com.moronlu18.customercreation.databinding.FragmentCustomerCreationBinding
import com.moronlu18.invoice.ui.MainActivity


class CustomerCreation : Fragment() {
    private var _binding: FragmentCustomerCreationBinding? = null
    private val binding get() = _binding!!
    private lateinit var launcher: ActivityResultLauncher<String>
    private var selectimgUri: Uri? = null

    private val viewModel: CustomerViewModel by viewModels()
    private val args: CustomerCreationArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCustomerCreationBinding.inflate(inflater, container, false)

        binding.viewmodelcustomercreation = this.viewModel
        binding.lifecycleOwner = this

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setUpGallery()

        setUpFab()

        //El observable
        viewModel.getState().observe(viewLifecycleOwner, Observer {
            when (it) {
                CustomerCreationState.NameIsMandatory -> setNameCustomerEmptyError()
                CustomerCreationState.EmailEmptyError -> setEmailEmptyError()
                CustomerCreationState.InvalidEmailFormat -> setEmailFormatError()
                CustomerCreationState.OnSuccess -> onSuccessCreate()
            }
        })

        binding.customerCreationTvIdCustomer.text = viewModel.getNextCustomerId().toString()
        //binding.customerCreationId.addTextChangedListener(CcWatcher(binding.customerCreationTilIdCustomer))
        binding.customerCreationTietNameCustomer.addTextChangedListener(CcWatcher(binding.customerCreationTilNameCustomer))
        binding.customerCreationTietEmailCustomer.addTextChangedListener(CcWatcher(binding.customerCreationTilCustomerEmail))
        //binding.customerCreationCcp.changeDefaultLanguage(CountryCodePicker.Language.DUTCH)
        //binding.customerCreationCcp.setAutoDetectedCountry(true)
        val customer: Customer? = args.customnav

        if (customer != null) {
            setUpEditMode(customer)
        }
    }

    /**
     * Configura la interfaz y establece los valores para el modo de edición.
     */

    private fun setUpEditMode(customerEdit: Customer) {
        println(customerEdit.email.toString())

        binding.customerCreationTvIdCustomer.text = customerEdit.id.value.toString()
        viewModel.nameCustomer.value = customerEdit.name
        viewModel.emailCustomer.value = customerEdit.email.toString()


        binding.customerCreationTietAddress.setText(customerEdit.address)

        val phone = customerEdit.phone
        val spaceIndex = phone?.indexOf(" ")

        binding.customerCreationCcp.setCountryForPhoneCode(
            spaceIndex?.let { phone.substring(0, it).toInt() } ?: 34

        )
        binding.customerCreationTietPhone.setText(
            spaceIndex?.let { phone.substring(it + 1) } ?: phone
        )

        binding.customerCreationTietCity.setText(customerEdit.city)

        if (customerEdit.photo != null) {
            binding.customerCreationImgcAvatar.setImageURI(customerEdit.photo)
        } else {
            binding.customerCreationImgcAvatar.setImageResource(R.drawable.kiwidinero)
        }
    }


    /**
     * Se le llama en caso de éxito.
     * Realiza las acciones necesarias para la creación o edición de un cliente si tiene éxito.
     */
    private fun onSuccessCreate() {

        val phone: String? = if (binding.customerCreationTietPhone.text.toString().isNotBlank()) {
            String.format(
                binding.customerCreationCcp.selectedCountryCodeWithPlus + " " +
                        binding.customerCreationTietPhone.text.toString()
            )
        } else {
            null
        }
        val customer: Customer? = args.customnav

        if (selectimgUri == null) {
               selectimgUri = defaulImageUri(R.drawable.kiwidinero)
           }
        if (customer != null) {

            customer.name = binding.customerCreationTietNameCustomer.text.toString()
            customer.email = Email(binding.customerCreationTietEmailCustomer.text.toString())
            customer.phone = phone
            customer.city =
                if (binding.customerCreationTietCity.text.isNullOrBlank()) null else binding.customerCreationTietCity.text.toString()
            customer.address =
                if (binding.customerCreationTietAddress.text.isNullOrBlank()) null else binding.customerCreationTietAddress.text.toString()
            customer.photo = selectimgUri
            //customer.photo = getbitMap(binding.customerCreationImgcAvatar)

            viewModel.updateCustomer(customer)

        } else {
            val id = viewModel.getNextCustomerId()
            val newCustomer = Customer.create(
                id = id,
                name = binding.customerCreationTietNameCustomer.text.toString(),
                email = Email(binding.customerCreationTietEmailCustomer.text.toString()),
                phone = phone,
                city = if (binding.customerCreationTietCity.text.isNullOrBlank()) null else binding.customerCreationTietCity.text.toString(),
                address = if (binding.customerCreationTietAddress.text.isNullOrBlank()) null else binding.customerCreationTietAddress.text.toString(),
                photo = selectimgUri
                //photo = getbitMap(binding.customerCreationImgcAvatar)
            )
            viewModel.addCustomer(newCustomer)
            //viewModel.sortRefresh()
        }
        findNavController().popBackStack()
    }

    private fun defaulImageUri(resourceId: Int): Uri {
        return Uri.parse("android.resource://${requireContext().packageName}/$resourceId")
    }
    private fun loadSelectedImage(uri: Uri) {
        Glide.with(this)
            .load(uri)
            .into(binding.customerCreationImgcAvatar)
    }


    private fun setUpGallery() {

        launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                loadSelectedImage(uri)
                //binding.customerCreationImgcAvatar.setImageURI(uri)
                onImageSelected(uri)
            }
        }
        binding.customerCreationImgbtnCustomer.setOnClickListener {
            launcher.launch("image/*")
        }
    }

    private fun onImageSelected(uri: Uri) {
        selectimgUri = uri
    }



    /**
     * Configura el launcher para el resultado de la galería.
     */
    /*private fun setUpGallery() {

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data: Intent? = it.data
                val imageUri = data?.data
                binding.customerCreationImgcAvatar.setImageURI(imageUri)
                onImageSelected(imageUri!!)
            }
        }
    }*/

    /**
     * Abre la galería para permitir al usuario seleccionar una imagen del dispositivo.
     */
    /*private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        launcher.launch(intent)
    }*/


    /**
     * Configura la visibilidad del botón flotante.
     * En este fragment se ha ocultado.
     */
    private fun setUpFab() {
        (requireActivity() as? MainActivity)?.fab?.apply {
            visibility = View.GONE
        }
    }

    /**
     * TextWatcher para anular el mensaje de error en un TextInputLayout.
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
     * Muestra un mensaje de error cuando el nombre del cliente está vacío.
     */
    private fun setNameCustomerEmptyError() {
        binding.customerCreationTilNameCustomer.error = getString(R.string.customer_error_EmptyName)
        binding.customerCreationTilNameCustomer.requestFocus()
    }

    /**
     * Muestra un mensaje de error cuando el correo electrónico está vacío.
     */
    private fun setEmailEmptyError() {
        binding.customerCreationTilCustomerEmail.error =
            getString(R.string.customer_error_EmptyEmail)
        binding.customerCreationTilCustomerEmail.requestFocus()
    }

    /**
     * Muestra un mensaje de error cuando el formato del correo electrónico es incorrecto.
     */
    private fun setEmailFormatError() {
        binding.customerCreationTilCustomerEmail.error =
            getString(R.string.customer_error_FormatEmail)
        binding.customerCreationTilCustomerEmail.requestFocus()
    }

    /**
     * Liberamos la referencia al binding.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}