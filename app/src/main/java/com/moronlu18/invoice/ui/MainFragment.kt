package com.moronlu18.invoice

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.moronlu18.invoice.databinding.FragmentMainBinding


class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imvAlert.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    binding.imvAlert.setImageResource(R.drawable.btnalertpressed)
                }
                MotionEvent.ACTION_UP,MotionEvent.ACTION_CANCEL -> {
                    binding.imvAlert.setImageResource(R.drawable.btnalert)
                }
            }
            false
        }
        //Customer
        binding.imvAlert.setOnClickListener {

        }

        binding.cvSigIn.btnAnimationNav(R.id.action_mainFragment_to_featureAccountSignIn)
        binding.cvSignUp.btnAnimationNav(R.id.action_mainFragment_to_featureAccountSignUp)
        binding.cvCustomer.btnAnimationNav(R.id.action_mainFragment_to_CustomerListFragment)
        binding.cvInvoice.btnAnimationNav(R.id.action_mainFragment_to_InvoiceListFragment)
        binding.cvItem.btnAnimationNav(R.id.action_mainFragment_to_ItemListFragment)
        binding.cvTask.btnAnimationNav(R.id.action_mainFragment_to_TaskListFragment)

    }

    @SuppressLint("ClickableViewAccessibility")
    fun View.btnAnimationNav(idDestination:Int) {
        setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.animate().scaleX(0.90f).scaleY(0.90f).setDuration(100).start()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100).start()
                }
            }
            false
        }

        setOnClickListener {
            findNavController().navigate(idDestination)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}