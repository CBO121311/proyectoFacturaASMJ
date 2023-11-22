package com.moronlu18.customer.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.moronlu18.customercreation.databinding.FragmentCustomerCreationBinding

class CustomerCreation : Fragment() {
    private var _binding: FragmentCustomerCreationBinding? = null
    private val binding get() = _binding!!

    private lateinit var launcher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCustomerCreationBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //binding.customerCreationImgbtnCustomer.setOnClickListener(requestPermission())

        /*binding.customerCreationImgbtnCustomer.setOnClickListener {
            openGallery()
        }*/

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data: Intent? = it.data
                val imageUri = data?.data
                binding.customerCreationImgcAvatar.setImageURI(imageUri)
            }
        }



        binding.customerCreationBtnSave.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager

            fragmentManager.popBackStack()
        }
    }


    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        launcher.launch(intent)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}