package com.jcasrui.item.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jcasrui.item.usecase.ItemDetailViewModel
import com.moronlu18.accounts.entity.Item
import com.moronlu18.invoice.base.BaseFragmentDialog
import com.moronlu18.invoice.ui.MainActivity
import com.moronlu18.itemcreation.R
import com.moronlu18.itemcreation.databinding.FragmentItemDetailBinding

class ItemDetail : Fragment(), MenuProvider {

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
        if (article.photo != null) {
            binding.itemDetailCvImg.setImageResource(article.photo!!)
        } else {
            binding.itemDetailCvImg.setImageResource(R.drawable.cart)
        }
        binding.itemDetailTvContentName.text = article.name
        binding.itemDetailTvContentDescription.text = article.description
        binding.itemDetailTvContentType.text = article.type.name
        binding.itemDetailTvContentVat.text = article.vat.name
        binding.itemDetailTvContentRate.text = article.price.toString()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpFab()
        setUpToolbar()

        viewModel.getState().observe(viewLifecycleOwner, Observer {
            when (it) {
                ItemDetailState.OnSuccess -> onSuccess()
                ItemDetailState.ReferencedItem -> showReferencedItem()
            }
        })

        binding.itemDetailBtnSave.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.popBackStack()
        }
    }

    private fun onSuccess() {
        val item = args.item

        viewModel.let {
            it.id.value = item.id.toString()
            it.type.value = item.type.name
            it.vat.value = item.vat.name
            it.name.value = item.description.toString()
            it.price.value = item.price.toString()
            it.description.value = item.description.toString()
        }

        if (item.photo != null) {
            binding.itemDetailCvImg.setImageResource(item.photo!!)
        } else {
            binding.itemDetailCvImg.setImageResource(R.drawable.cart)
        }
    }

    private fun showReferencedItem() {
        val dialog = BaseFragmentDialog.newInstance(
            getString(R.string.title_warning),
            getString(R.string.content_warning)
        )
        dialog.show(childFragmentManager, "edit_dialog")
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
        menuInflater.inflate(R.menu.menu_item_detail, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.menuItemDetail_actionEdit -> {
                editItem()     // editar artículo
                true
            }

            R.id.menuItemDetail_actionDelete -> {
                deleteItem()   // borrar artículo
                true
            }

            else -> false
        }
    }

    /**
     * Función que edita el artículo
     */
    private fun editItem() {
        val item = args.item
        findNavController().navigate(ItemDetailDirections.actionItemDetailToItemCreation(item))
    }

    /**
     * Eliminar un artículo
     */
    private fun deleteItem() {
        val item = args.item

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
                    findNavController().popBackStack()
                }
            }
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