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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jcasrui.item.usecase.ItemDetailViewModel
import com.moronlu18.accounts.entity.Item
import com.moronlu18.invoice.base.BaseFragmentDialog
import com.moronlu18.invoice.ui.MainActivity
import com.moronlu18.itemcreation.R
import com.moronlu18.itemcreation.databinding.FragmentItemDetailBinding

class ItemDetail : Fragment(), MenuProvider{

    private val item: Item? = null
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
        binding.itemDetailCvImg.setImageResource(article.photo)
        binding.itemDetailTvContentName.text = article.name
        binding.itemDetailTvContentDescription.text = article.description
        binding.itemDetailTvContentType.text = article.type.name
        binding.itemDetailTvContentVat.text = article.vat.name
        binding.itemDetailTvContentRate.text = article.price.toString()

        /*if (article.taxable) {
            binding.itemDetailTvTaxable.text = "Impuestos incluidos"
        } else {
            binding.itemDetailTvTaxable.text = "Sin impuestos"
        }*/

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpFab()
        setUpToolbar()

        binding.itemDetailBtnSave.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.popBackStack()
        }
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
                //editItem(item!!)     // editar artículo
                true
            }

            R.id.menuItemDetail_actionDelete -> {
                //deleteItem()   // borrar artículo
                true
            }

            else -> false
        }
    }

    /**
     * Función que edita el artículo
     */
    private fun editItem(item: Item) {
        val positionItem = viewModel.getPosition(item)

        if (viewModel.deleteItemSafe(item)) {
            val bundle = Bundle()
            bundle.putInt("itemPosition", positionItem)
            parentFragmentManager.setFragmentResult("itemKey", bundle)
            findNavController().navigate(R.id.action_itemDetail_to_itemCreation)
        }
    }

    /**
     * Eliminar un artículo
     */

    private fun deleteItem() {
        if (viewModel.deleteItemSafe(item!!)) {
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
                    //adapter.notifyItemRemoved(position)
                    viewModel.deleteItemSafe(item!!)
                    Handler(Looper.getMainLooper()).postDelayed({
                        findNavController().popBackStack()
                    }, 100)
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