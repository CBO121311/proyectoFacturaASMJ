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

    private var item: Item? = null
    private var positionItem: Int = 0
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
        binding.itemDetailCvImg.setImageResource(article.photo!!)
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

        parentFragmentManager.setFragmentResultListener("detailkey", this,
            FragmentResultListener { _, result ->
                positionItem = result.getInt("detailposition")
                item = viewModel.getPositionItem(positionItem)
            }
        )

        binding.itemDetailBtnSave.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.popBackStack()
        }
    }

    private fun onSuccess() {
        viewModel.let {
            it.id.value = item!!.id.toString()
            it.type.value = item!!.type.name
            it.vat.value = item!!.vat.name
            it.name.value = item!!.description.toString()
            it.price.value = item!!.price.toString()
            it.description.value = item!!.description.toString()
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
                    viewModel.deleteItem(item!!)
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