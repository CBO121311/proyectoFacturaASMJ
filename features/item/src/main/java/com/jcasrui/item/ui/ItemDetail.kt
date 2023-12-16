package com.jcasrui.item.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.jcasrui.item.usecase.ItemDetailViewModel
import com.moronlu18.accounts.entity.Item
import com.moronlu18.itemcreation.databinding.FragmentItemDetailBinding

class ItemDetail : Fragment() {

    private val args: ItemDetailArgs by navArgs()
    private var _binding: FragmentItemDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ItemDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentItemDetailBinding.inflate(inflater, container, false)
        binding.viewmodelitemdetail = this.viewModel
        binding.lifecycleOwner = this

        val article: Item = args.item
        binding.itemDetailCvImg.setImageResource(article.image)
        binding.itemDetailTvContentName.text = article.name
        binding.itemDetailTvContentDescription.text = article.description
        binding.itemDetailTvContentType.text = article.type.name
        binding.itemDetailTvContentRate.text = article.rate.toString()

        if (article.taxable) {
            binding.itemDetailTvTaxable.text = "Impuestos incluidos"
        } else {
            binding.itemDetailTvTaxable.text = "Sin impuestos"
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.itemDetailBtnSave.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}