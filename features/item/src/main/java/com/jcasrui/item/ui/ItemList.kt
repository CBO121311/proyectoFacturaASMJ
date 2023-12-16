package com.jcasrui.item.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jcasrui.item.adapter.ItemAdapter
import com.jcasrui.item.usecase.ItemListViewModel
import com.moronlu18.accounts.entity.Item
import com.moronlu18.invoice.base.BaseFragmentDialog
import com.moronlu18.invoice.ui.MainActivity
import com.moronlu18.itemcreation.R
import com.moronlu18.itemcreation.databinding.FragmentItemListBinding

class ItemList : Fragment(), MenuProvider {

    private var _binding: FragmentItemListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ItemAdapter
    private val viewModel: ItemListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentItemListBinding.inflate(inflater, container, false)

        binding.viewmodelitemlist = this.viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()

        initRecyclerViewItem()

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_itemList_to_itemCreation)
        }

        viewModel.getState().observe(viewLifecycleOwner, Observer {
            when (it) {
                ItemListState.NoData -> showNoData()
                is ItemListState.Success -> onSuccess(it.dataset)
                ItemListState.ReferencedItem -> showReferencedItem()
                else -> {}
            }
        })
    }

    private fun initRecyclerViewItem() {
        val manager = LinearLayoutManager(requireContext())

        adapter = ItemAdapter(
            itemList = viewModel.getItem(),
            onClickListener = { item -> onItemSelected(item) },
            onClickEdit = { position -> onEditItem(position) },
            onClickDelete = { position -> onDeleteItem(position) }
        )
        binding.itemListRvItems.layoutManager = manager
        binding.itemListRvItems.adapter = adapter
    }

    private fun onEditItem(position: Int) {
        val item = viewModel.getPositionItem(position)

        if (viewModel.deleteItemSafe(item)) {
            val bundle = Bundle()

            bundle.putInt("itemPosition", position)
            parentFragmentManager.setFragmentResult("itemKey", bundle)
            findNavController().navigate(R.id.action_itemList_to_itemCreation)
        }
    }

    private fun showReferencedItem() {
        findNavController().navigate(
            ItemListDirections.actionItemListToBaseFragmentDialogWarning(
                getString(R.string.title_warning),
                getString(R.string.content_warning)
            )
        )
    }

    private fun onDeleteItem(position: Int) {
        val item = viewModel.getPositionItem(position)

        if (viewModel.deleteItemSafe(item)) {
            findNavController().navigate(
                ItemListDirections.actionItemListToBaseFragmentDialog(
                    getString(R.string.title_deleteItem),
                    getString(R.string.content_deleteItem)
                )
            )

            parentFragmentManager.setFragmentResultListener(
                BaseFragmentDialog.request,
                viewLifecycleOwner
            ) { _, result ->
                val success = result.getBoolean(BaseFragmentDialog.result, false)
                if (success) {
                    adapter.notifyItemRemoved(position)
                    viewModel.deleteItem(position)
                    if (viewModel.getItem().isEmpty()) {
                        showNoData()
                    }
                }
            }
        }
    }

    private fun onItemSelected(item: Item) {
        findNavController().navigate(ItemListDirections.actionItemListToItemDetail(item))
    }

    private fun showNoData() {
        binding.itemListConstraintLayoutEmpty.visibility = View.VISIBLE
        binding.itemListRvItems.visibility = View.GONE
    }

    private fun onSuccess(dataset: ArrayList<Item>) {
        binding.itemListConstraintLayoutEmpty.visibility = View.GONE
        binding.itemListRvItems.visibility = View.VISIBLE
        adapter.update(dataset)
    }

    /**
     * Personaliza el comportamiento de la Toolbar
     */
    private fun setUpToolbar() {
        (requireActivity() as? MainActivity)?.toolbar?.apply {
            visibility = View.VISIBLE
        }
        val menuhost: MenuHost = requireActivity()
        menuhost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    /**
     * Añade las opciones del menu
     */
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_item_list, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.menuItemDetail_actionRefresh -> {
                viewModel.getItem()     // orden natural
                return true
            }

            R.id.menuItemDetail_actionSort -> {
                adapter.sort()          // orden personalizado
                return true
            }

            else -> false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.onSuccess()
        _binding = null
    }
}