package com.jcasrui.item.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.moronlu18.accounts.entity.Item
import com.moronlu18.itemcreation.R
import com.moronlu18.itemcreation.databinding.FragmentItemDetailBinding

class ItemDetail : Fragment() {

    private val args: ItemDetailArgs by navArgs()
    private var _binding: FragmentItemDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentItemDetailBinding.inflate(inflater, container, false)

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

        binding.itemDetailBtnSave.setOnClickListener {
            val fragmenManager = requireActivity().supportFragmentManager
            fragmenManager.popBackStack()
        }

        return binding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_item_detail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}