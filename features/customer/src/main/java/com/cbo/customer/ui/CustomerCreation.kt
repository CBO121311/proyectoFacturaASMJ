package com.cbo.customer.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
import com.moronlu18.customercreation.R
import com.moronlu18.customercreation.databinding.FragmentCustomerCreationBinding
import com.moronlu18.invoice.ui.MainActivity


class CustomerCreation : Fragment() {
    private var _binding: FragmentCustomerCreationBinding? = null
    private val binding get() = _binding!!
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private val viewModel: CustomerViewModel by viewModels()
    private var editCustomerPos = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCustomerCreationBinding.inflate(inflater, container, false)

        binding.viewmodelcustomercreation = this.viewModel
        binding.lifecycleOwner = this
        viewModel.setEditorMode(false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpGallery()
        setUpFab()

        binding.customerCreationImgbtnCustomer.setOnClickListener {
            openGallery()
        }


        //Si es editar
        parentFragmentManager.setFragmentResultListener(
            "customkey", this, FragmentResultListener { _, result ->
                var posCustomer: Int = result.getInt("customposition")
                val customerEdit = viewModel.getCustomerByPosition(posCustomer)
                viewModel.setEditorMode(true)

                binding.customerCreationTietIdCustomer.setText(customerEdit.id.toString())
                binding.customerCreationTietEmailCustomer.setText(customerEdit.email.toString())
                binding.customerCreationTietNameCustomer.setText(customerEdit.name)
                binding.customerCreationTietAddress.setText(customerEdit.address)
                binding.customerCreationTietPhone.setText(customerEdit.phone)
                binding.customerCreationTietCity.setText(customerEdit.city)

                if (customerEdit.phototrial != null) {
                    binding.customerCreationImgcAvatar.setImageResource(customerEdit.phototrial!!)
                } else {
                    binding.customerCreationImgcAvatar.setImageBitmap(customerEdit.photo)
                }

                binding.customerCreationLlid.visibility = View.VISIBLE
                editCustomerPos = posCustomer

                viewModel.prevCustomer = customerEdit
            }
        )

        //El observable
        viewModel.getState().observe(viewLifecycleOwner, Observer {
            when (it) {
                CustomerCreationState.InvalidId -> setInvalidIdError()
                CustomerCreationState.NameIsMandatory -> setNameCustomerEmptyError()
                CustomerCreationState.EmailEmptyError -> setEmailEmptyError()
                CustomerCreationState.InvalidEmailFormat -> setEmailFormatError()
                CustomerCreationState.OnSuccess -> onSuccessCreate()
                else -> {}
            }
        })
        binding.customerCreationTietIdCustomer.addTextChangedListener(CcWatcher(binding.customerCreationTilIdCustomer))
        binding.customerCreationTietNameCustomer.addTextChangedListener(CcWatcher(binding.customerCreationTilNameCustomer))
        binding.customerCreationTietEmailCustomer.addTextChangedListener(CcWatcher(binding.customerCreationTilCustomerEmail))
    }

    /**
     * Se le llama en caso de éxito.
     * Realiza las acciones necesarias para la creación o edición de un cliente si tiene éxito.
     */
    private fun onSuccessCreate() {
        val id = binding.customerCreationTietIdCustomer.text.toString().toIntOrNull()
            ?: viewModel.getNextCustomerId()

        if (viewModel.getEditorMode()) {

            val updatedCustomer = Customer(
                id = id,
                name = binding.customerCreationTietNameCustomer.text.toString(),
                email = Email(binding.customerCreationTietEmailCustomer.text.toString()),
                phone = binding.customerCreationTietPhone.text.toString(),
                city = binding.customerCreationTietCity.text.toString(),
                address = binding.customerCreationTietAddress.text.toString(),
                photo = getbitMap(binding.customerCreationImgcAvatar),
                phototrial = null
            )

            viewModel.updateCustomer(updatedCustomer, editCustomerPos)

        } else {
            val customer = Customer(
                id = viewModel.getNextCustomerId(),
                name = binding.customerCreationTietNameCustomer.text.toString(),
                email = Email(binding.customerCreationTietEmailCustomer.text.toString()),
                phone = binding.customerCreationTietPhone.text.toString(),
                city = binding.customerCreationTietCity.text.toString(),
                address = binding.customerCreationTietAddress.text.toString(),
                photo = getbitMap(binding.customerCreationImgcAvatar),
                phototrial = null
            )
            viewModel.addCustomer(customer)
            viewModel.sortRefresh()
        }
        findNavController().popBackStack()
    }


    /**
     * Configura el launcher para el resultado de la galería.
     */
    private fun setUpGallery() {

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data: Intent? = it.data
                val imageUri = data?.data
                binding.customerCreationImgcAvatar.setImageURI(imageUri)
            }
        }
    }


    /**
     * Obtiene un Bitmap a partir de la imagen actual de la ImageView.
     */

    private fun getbitMap(imageView: ImageView): Bitmap {
        val bitmap = Bitmap.createBitmap(imageView.width, imageView.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        imageView.draw(canvas)
        return bitmap
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
     * Muestra un mensaje de error cuando el id del cliente no es válido.
     */
    private fun setInvalidIdError() {
        binding.customerCreationTilIdCustomer.error = getString(R.string.customer_error_Invalidid)
        binding.customerCreationTietIdCustomer.requestFocus()
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
     * Abre la galería para permitir al usuario seleccionar una imagen del dispositivo.
     */
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        launcher.launch(intent)
    }

    /**
     * Liberamos la referencia al binding.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}