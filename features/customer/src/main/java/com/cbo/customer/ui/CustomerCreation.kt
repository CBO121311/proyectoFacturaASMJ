package com.cbo.customer.ui



import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.cbo.customer.usecase.CustomerViewModel
import com.google.android.material.textfield.TextInputLayout
import com.moronlu18.data.customer.Customer
import com.moronlu18.data.base.Email
import com.moronlu18.customercreation.R
import com.moronlu18.customercreation.databinding.FragmentCustomerCreationBinding
import com.moronlu18.invoice.ui.MainActivity
import com.moronlu18.invoice.utils.createNotificationChannel
import com.moronlu18.invoice.utils.sendNotification


class CustomerCreation : Fragment() {
    private var _binding: FragmentCustomerCreationBinding? = null
    private val binding get() = _binding!!
    private lateinit var launcher: ActivityResultLauncher<Intent>
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
        createNotificationChannel(CHANNEL_ID, requireContext())
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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

        when {
            customer != null -> {
                setUpEditMode(customer)
            }
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

        when {
            customerEdit.photo != null -> {
                binding.customerCreationImgcAvatar.setImageURI(customerEdit.photo)
            }

            else -> {
                binding.customerCreationImgcAvatar.setImageResource(R.drawable.kiwidinero)
            }
        }
    }


    /**
     * Se le llama en caso de éxito.
     * Realiza las acciones necesarias para la creación o edición de un cliente si tiene éxito.
     */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun onSuccessCreate() {

        val phone: String? = when {
            binding.customerCreationTietPhone.text.toString().isNotBlank() -> {
                String.format(
                    binding.customerCreationCcp.selectedCountryCodeWithPlus + " " +
                            binding.customerCreationTietPhone.text.toString()
                )
            }
            else -> {
                null
            }
        }
        val customer: Customer? = args.customnav

        val name = binding.customerCreationTietNameCustomer.text.toString()

        when {
            customer != null -> {

                customer.name = name
                customer.email = Email(binding.customerCreationTietEmailCustomer.text.toString())
                customer.phone = phone
                customer.city =
                    when {
                        binding.customerCreationTietCity.text.isNullOrBlank() -> null
                        else -> binding.customerCreationTietCity.text.toString()
                    }
                customer.address =
                    when {
                        binding.customerCreationTietAddress.text.isNullOrBlank() -> null
                        else -> binding.customerCreationTietAddress.text.toString()
                    }

                customer.photo = selectimgUri ?: customer.photo

                viewModel.updateCustomer(customer)
                sendNotification(
                    CHANNEL_ID, requireContext(),
                    getString(R.string.cc_notification_edit_title),
                    getString(R.string.cc_notification_content, name)
                )

            }

            else -> {

                selectimgUri ?: defaulImageUri(R.drawable.kiwidinero)

                val id = viewModel.getNextCustomerId()
                val newCustomer = Customer.create(
                    id = id,
                    name = name,
                    email = Email(binding.customerCreationTietEmailCustomer.text.toString()),
                    phone = phone,
                    city = if (binding.customerCreationTietCity.text.isNullOrBlank()) null else binding.customerCreationTietCity.text.toString(),
                    address = if (binding.customerCreationTietAddress.text.isNullOrBlank()) null else binding.customerCreationTietAddress.text.toString(),
                    photo = selectimgUri
                )
                viewModel.addCustomer(newCustomer)
                sendNotification(
                    CHANNEL_ID, requireContext(),
                    getString(R.string.cc_notification_creation_title),
                    getString(R.string.cc_notification_content, name)
                )
            }
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

        launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val uri = result.data?.data
                    if (uri != null) {
                        // Solicitar permiso persistente para el URI seleccionado
                        requireContext().contentResolver.takePersistableUriPermission(
                            uri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )
                        loadSelectedImage(uri)
                        onImageSelected(uri)
                    }
                }
            }

        binding.customerCreationImgbtnCustomer.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            launcher.launch(intent)
        }
    }

    private fun onImageSelected(uri: Uri) {
        selectimgUri = uri
    }


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

    companion object {
        const val CHANNEL_ID = "customer_channel"
    }
}
