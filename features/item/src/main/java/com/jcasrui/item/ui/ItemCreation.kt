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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.jcasrui.item.usecase.ItemViewModel
import com.moronlu18.accounts.entity.Item
import com.moronlu18.accounts.enum.ItemType
import com.moronlu18.accounts.repository.ItemProvider
import com.moronlu18.itemcreation.R
import com.moronlu18.itemcreation.databinding.FragmentItemCreationBinding

class ItemCreation : Fragment() {

    private var _binding: FragmentItemCreationBinding? = null
    private val binding get() = _binding!!
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private val viewModel: ItemViewModel by viewModels()
    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentItemCreationBinding.inflate(inflater, container, false)

        binding.viewmodelitemcreation = this.viewModel
        binding.lifecycleOwner = this

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
            val fragmenManager = requireActivity().supportFragmentManager
            fragmenManager.popBackStack()
        }

        binding.itemCreationTieName.addTextChangedListener(textWatcher(binding.itemCreationTilName))
    }

    private fun onSuccessCreate() {
        val name = binding.itemCreationTieName.text.toString()
        val description = binding.itemCreationTieDescription.text.toString()
        val rate = binding.itemCreationTieRate.text.toString().toDouble()
        val taxable = binding.itemCreationSwitchTaxable.isChecked

        val item = Item(
            id = ItemProvider.dataSetItem.size + 1,
            image = R.drawable.cart,
            name = name,
            description = description.ifEmpty { "Sin descripción" },
            type = itemType(),
            rate = rate,
            taxable = taxable
        )

        ItemProvider.dataSetItem.add(item)
        findNavController().popBackStack()
    }

    private fun itemType(): ItemType {
        return when (binding.itemCreationSpItemType.selectedItemId) {
            0L -> ItemType.ARTÍCULO
            1L -> ItemType.SERVICIO
            else -> ItemType.ARTÍCULO
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

    inner class textWatcher(private val til: TextInputLayout) : TextWatcher {
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