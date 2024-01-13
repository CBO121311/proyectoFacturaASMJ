package com.moronlu18.invoice.ui

import com.moronlu18.invoice.R
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.moronlu18.invoice.Locator
import com.moronlu18.invoice.base.BaseFragmentDialog

import com.moronlu18.invoice.databinding.FragmentMainBinding


class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpFab()

        (requireActivity() as? MainActivity)?.toolbar?.setNavigationOnClickListener {
            if (findNavController().currentDestination?.id == R.id.mainFragment) {
                //activity?.finish()
                activity?.moveTaskToBack(true)
            } else {
                findNavController().popBackStack()
            }
        }

        binding.cvCustomer.btnAnimationNav(R.id.action_mainFragment_to_nav_graph_customer)
        binding.cvTask.btnAnimationNav(R.id.action_mainFragment_to_nav_graph_task)
        binding.cvInvoice.btnAnimationNav(R.id.action_mainFragment_to_nav_graph_invoice)
        binding.cvItem.btnAnimationNav(R.id.action_mainFragment_to_nav_graph_item)
        binding.cvSigIn.btnAnimationNav(R.id.action_mainFragment_to_nav_graph_account)


        binding.cvSignOut.btnAnimationNav(-1)
        binding.cvSignUp.btnAnimationNav(R.id.action_mainFragment_to_nav_graph_fromsignup)


        binding.cvAbout.btnAnimationNav(R.id.action_mainFragment_to_about)


        parentFragmentManager.setFragmentResultListener(
            BaseFragmentDialog.request,
            viewLifecycleOwner
        ) { _, result ->
            val success = result.getBoolean(BaseFragmentDialog.result, false)
            if (success) {
                requireActivity().finish()
            }
        }

        applyTextStylePreference()

    }
    /**
     * Aplica el estilo del texto a los TextView en función de la preferencia de tamaño de texto.
     */
    private fun applyTextStylePreference() {
        val bigOrNo = Locator.settingsPreferencesRepository.getBoolean("key_setting_text", false)

        val tvxDashboard = listOf(
            binding.buttonTxvCustomer,
            binding.buttonTxvItem,
            binding.buttonTxvSignIn,
            binding.buttonTxvSignUp,
            binding.buttonTxvTask,
            binding.buttonTxvSignOut,
            binding.buttonTxvAbout,
            binding.buttonTxvInvoice
        )

        val tvxStyle =
            if (bigOrNo) R.style.dashboard_button_txv_big else R.style.dashboard_button_txv_normal
        tvxDashboard.forEach { it.setTextAppearance(tvxStyle) }
    }
    
    
    private fun setUpFab() {
        (requireActivity() as? MainActivity)?.fab?.apply {
            visibility = View.VISIBLE
            setImageResource(R.drawable.ic_action_alert)
            setOnClickListener { view ->

                if (findNavController().currentDestination?.id == R.id.mainFragment) {
                    Snackbar.make(view, "Implementación futura", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                } else {
                    Snackbar.make(
                        view,
                        "No me dejes así, pon una función o hazla no visible <_<\"",
                        Snackbar.LENGTH_LONG
                    )
                        .setAction("Action", null).show()
                }
            }
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    fun View.btnAnimationNav(idDestination: Int) {
        setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {

                    val pushDown = AnimatorInflater.loadAnimator(
                        requireContext(),
                        R.animator.anim_btn_push
                    ) as AnimatorSet
                    pushDown.setTarget(v)
                    pushDown.start();
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {

                    val pushUp = AnimatorInflater.loadAnimator(
                        requireContext(),
                        R.animator.anim_btn_up
                    ) as AnimatorSet
                    pushUp.setTarget(v)
                    pushUp.start();
                }
            }
            false
        }
        if (idDestination == -1) {
            setOnClickListener {
                findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToBaseFragmentDialog(
                        getString(R.string.title_fragmentDialogExit),
                        getString(R.string.Content_fragmentDialogExit)
                    )
                )
            }
        } else {
            setOnClickListener {
                findNavController().navigate(idDestination)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}