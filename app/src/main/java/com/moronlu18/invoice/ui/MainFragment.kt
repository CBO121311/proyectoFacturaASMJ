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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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