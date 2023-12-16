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
import com.google.android.material.textfield.TextInputLayout
import com.jcasrui.item.usecase.ItemCreationViewModel
import com.moronlu18.accounts.entity.Item
import com.moronlu18.accounts.enum.ItemType
import com.moronlu18.itemcreation.R
import com.moronlu18.itemcreation.databinding.FragmentItemCreationBinding

class ItemCreation : Fragment() {

    private var _binding: FragmentItemCreationBinding? = null
    private val binding get() = _binding!!
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private val viewModel: ItemCreationViewModel by viewModels()
    private var selectedImageUri: Uri? = null
    private var editPosItem = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentItemCreationBinding.inflate(inflater, container, false)

        binding.viewmodelitemcreation = this.viewModel
        binding.lifecycleOwner = this
        viewModel.setEditor(false)

        parentFragmentManager.setFragmentResultListener(
            "itemKey", this, FragmentResultListener { _, result ->
                val positionItem: Int = result.getInt("itemPosition")
                val itemEdit = viewModel.getPositionItem(positionItem)
                viewModel.setEditor(true)

                binding.itemCreationTieName.setText(itemEdit.name)
                binding.itemCreationTieDescription.setText(itemEdit.description)
                binding.itemCreationSpItemType.setSelection(itemType(itemEdit))
                binding.itemCreationTieRate.setText(itemEdit.rate.toString())
                binding.itemCreationSwitchTaxable.isChecked

                editPosItem = positionItem

                viewModel.prevItem = itemEdit
            }
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                ItemState.NameIsMandatory -> setNameItemEmptyError()
                ItemState.RateIsMandatory -> setRateItemEmptyError()
                ItemState.OnSuccess -> onSuccessCreate()
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
        val rate = binding.itemCreationTieRate.text.toString().toDouble()
        val taxable = binding.itemCreationSwitchTaxable.isChecked

        if (viewModel.getEditor()) {
            val updateItem = Item(
                id = editPosItem + 1,
                image = R.drawable.cart,
                name = name,
                description = description.ifEmpty { "Sin descripción" },
                type = chooseItemType(),
                rate = rate,
                taxable = taxable
            )
            viewModel.updateItem(updateItem, editPosItem)
        } else {
            val item = Item(
                id = viewModel.getNextId(),
                image = R.drawable.cart,
                name = name,
                description = description.ifEmpty { "Sin descripción" },
                type = chooseItemType(),
                rate = rate,
                taxable = taxable
            )
            viewModel.addItem(item)
        }
        findNavController().popBackStack()
    }

    private fun chooseItemType(): ItemType {
        return when (binding.itemCreationSpItemType.selectedItemId) {
            0L -> ItemType.ARTÍCULO
            1L -> ItemType.SERVICIO
            else -> ItemType.ARTÍCULO
        }
    }

    private fun itemType(item: Item): Int {
        return when (item.type) {
            ItemType.ARTÍCULO -> 0
            ItemType.SERVICIO -> 1
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}