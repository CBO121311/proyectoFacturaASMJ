package com.jcasrui.item.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputLayout
import com.jcasrui.item.usecase.ItemCreationViewModel
import com.moronlu18.accounts.entity.Item
import com.moronlu18.data.base.ItemId
import com.moronlu18.data.item.ItemType
import com.moronlu18.data.item.VatItemType
import com.moronlu18.invoice.ui.MainActivity
import com.moronlu18.itemcreation.R
import com.moronlu18.itemcreation.databinding.FragmentItemCreationBinding

class ItemCreation : Fragment() {

    private var _binding: FragmentItemCreationBinding? = null
    private val binding get() = _binding!!
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private val viewModel: ItemCreationViewModel by viewModels()
    private var selectedImageUri: Uri? = null
    private val args: ItemCreationArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentItemCreationBinding.inflate(inflater, container, false)

        binding.viewmodelitemcreation = this.viewModel
        binding.lifecycleOwner = this

        val item = args.item
        if (item != null) {
            binding.itemCreationTieName.setText(item.name)
            binding.itemCreationTieDescription.setText(item.description)
            binding.itemCreationSpItemType.setSelection(itemType(item))
            binding.itemCreationSpVatType.setSelection(vatType(item))
            binding.itemCreationTieRate.setText(item.price.toString())
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpFab()

        // Abrir galería
        binding.itemCreationImgBtnAdd.setOnClickListener {
            pickPhoto()
        }

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data: Intent? = it.data
                selectedImageUri = data?.data
                binding.itemCreationCivImagenItem.setImageURI(selectedImageUri)
            }
        }

        viewModel.getState().observe(viewLifecycleOwner, Observer {
            when (it) {
                ItemCreationState.NameIsMandatory -> setNameItemEmptyError()
                ItemCreationState.RateIsMandatory -> setRateItemEmptyError()
                ItemCreationState.OnSuccess -> onSuccessCreate()
                else -> {}
            }
        })

        binding.itemCreationBtnSave.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.popBackStack()
        }

        binding.itemCreationTieName.addTextChangedListener(CreationTextWatcher(binding.itemCreationTilName))
        binding.itemCreationTieRate.addTextChangedListener(CreationTextWatcher(binding.itemCreationTilName))
    }

    private fun onSuccessCreate() {
        val name = binding.itemCreationTieName.text.toString()
        val description = binding.itemCreationTieDescription.text.toString()
        val price = binding.itemCreationTieRate.text.toString().toDouble()

        val itemArgs = args.item

        if (itemArgs != null) {
            val updateItem = Item(
                id = itemArgs.id,
                type = chooseItemType(),
                vat = chooseVatType(),
                name = name,
                price = price,
                description = description.ifEmpty { "Sin descripción" },
                photo = R.drawable.cart
            )
            viewModel.updateItem(updateItem)
        } else {
            val item = Item(
                id = ItemId(viewModel.getNextId()),
                type = chooseItemType(),
                vat = chooseVatType(),
                name = name,
                price = price,
                description = description.ifEmpty { "Sin descripción" },
                photo = R.drawable.cart
            )
            viewModel.addItem(item)
        }
        findNavController().popBackStack()
    }

    private fun chooseItemType(): ItemType {
        return when (binding.itemCreationSpItemType.selectedItemId) {
            0L -> ItemType.PRODUCT
            1L -> ItemType.SERVICE
            else -> ItemType.PRODUCT
        }
    }

    private fun itemType(item: Item): Int {
        return when (item.type) {
            ItemType.PRODUCT -> 0
            ItemType.SERVICE -> 1
        }
    }

    private fun chooseVatType(): VatItemType {
        return when (binding.itemCreationSpVatType.selectedItemId) {
            0L -> VatItemType.ZERO
            1L -> VatItemType.FOUR
            2L -> VatItemType.FIVE
            3L -> VatItemType.TEN
            4L -> VatItemType.TWENTYONE
            else -> VatItemType.TWENTYONE
        }
    }

    private fun vatType(item: Item): Int {
        return when (item.vat) {
            VatItemType.ZERO -> 0
            VatItemType.FOUR -> 1
            VatItemType.FIVE -> 2
            VatItemType.TEN -> 3
            VatItemType.TWENTYONE -> 4
        }
    }

    private fun setNameItemEmptyError() {
        binding.itemCreationTilName.error = "Indicar nombre del artículo"
        binding.itemCreationTilName.requestFocus()
    }

    private fun setRateItemEmptyError() {
        binding.itemCreationTilRate.error = "Indicar precio del artículo"
        binding.itemCreationTilRate.requestFocus()
    }

    private fun pickPhoto() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        launcher.launch(galleryIntent)
    }

    inner class CreationTextWatcher(private val til: TextInputLayout) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            til.error = null
        }
    }

    private fun setUpFab() {
        (requireActivity() as? MainActivity)?.fab?.apply {
            visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}