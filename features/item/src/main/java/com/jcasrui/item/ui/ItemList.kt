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
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
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

        viewModel.getItemList()

        var appBarConfiguration =
            AppBarConfiguration.Builder(R.id.itemList)
                .setOpenableLayout((requireActivity() as MainActivity).drawer)
                .build()

        NavigationUI.setupWithNavController(
            (requireActivity() as MainActivity).toolbar,
            findNavController(),
            appBarConfiguration
        )

        setUpFab()
        setUpToolbar()
        initRecyclerViewItem()

        viewModel.getState().observe(viewLifecycleOwner, Observer {
            when (it) {
                is ItemListState.Loading -> showProgressBar(it.value)
                ItemListState.NoData -> showNoData()
                is ItemListState.OnSuccess -> onSuccess()
                ItemListState.ReferencedItem -> showReferencedItem()
                else -> {}
            }
        })

        viewModel.allItem.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                showNoData()
            } else {
                onSuccess()
                it.let { adapter.submitList(it) }
            }
        }
    }

    private fun initRecyclerViewItem() {
        val manager = LinearLayoutManager(requireContext())

        adapter = ItemAdapter(
            onClickListener = { item -> onItemSelected(item) },
            //onClickEdit = { position -> onEditItem(position) },
            onClickDelete = { item -> onDeleteItem(item) }
        )
        binding.itemListRvItems.layoutManager = manager
        binding.itemListRvItems.adapter = adapter
    }

    /*
    private fun onEditItem(position: Int) {
        val item = viewModel.getPositionItem(position)

        if (viewModel.deleteItemSafe(item)) {
            val bundle = Bundle()

            bundle.putInt("itemPosition", position)
            parentFragmentManager.setFragmentResult("itemKey", bundle)
            findNavController().navigate(R.id.action_itemList_to_itemCreation)
        }
    }*/

    private fun showReferencedItem() {
        val dialog = BaseFragmentDialog.newInstance(
            getString(R.string.title_warning),
            getString(R.string.content_warning)
        )
        dialog.show(childFragmentManager, "edit_dialog")
    }

    private fun onDeleteItem(item: Item) {
        if (viewModel.deleteItemSafe(item)) {
            val dialog = BaseFragmentDialog.newInstance(
                getString(R.string.title_deleteItem),
                getString(R.string.content_deleteItem)
            )

            dialog.show(childFragmentManager, "delete_dialog")

            dialog.parentFragmentManager.setFragmentResultListener(
                BaseFragmentDialog.request,
                viewLifecycleOwner
            ) { _, result ->
                val success = result.getBoolean(BaseFragmentDialog.result)
                if (success) {
                    viewModel.deleteItem(item)
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

    private fun onSuccess() {
        binding.itemListConstraintLayoutEmpty.visibility = View.GONE
        binding.itemListRvItems.visibility = View.VISIBLE
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
            R.id.menuItemList_actionRefresh -> {
                viewModel.getItemList()
                adapter.sortId()
                return true
            }

            R.id.menuItemList_actionSort -> {
                adapter.sortName()
                return true
            }

            else -> false
        }
    }

    private fun setUpFab() {
        (requireActivity() as? MainActivity)?.fab?.apply {
            visibility = View.VISIBLE

            setOnClickListener {
                findNavController().navigate(R.id.action_itemList_to_itemCreation)
            }
        }
    }

    private fun showProgressBar(value: Boolean) {
        if (value) {
            findNavController().navigate(R.id.action_itemList_to_fragmentProgressDialogKiwi)
        } else {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}