package com.jcasrui.itemlist.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jcasrui.itemlist.adapter.ItemAdapter
import com.jcasrui.itemlist.data.Item
import com.jcasrui.itemlist.data.ItemProvider
import com.moronlu18.itemlist.databinding.FragmentItemListBinding

class ItemList : Fragment() {

    private var _binding : FragmentItemListBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentItemListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initReciclerView()
    }

    private fun initReciclerView() {
        val manager = LinearLayoutManager(requireContext())
        val decoration = DividerItemDecoration(requireContext(), manager.orientation)

        binding.itemListRvItems.layoutManager = manager
        binding.itemListRvItems.adapter = ItemAdapter(ItemProvider.itemList) { item ->
            onItemSelected(
                item
            )
        }
        binding.itemListRvItems.addItemDecoration(decoration)
    }

    fun onItemSelected(item: Item) {
        Toast.makeText(requireContext(), item.name, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}