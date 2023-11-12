package com.cbo.customercreation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.moronlu18.customercreation.databinding.FragmentCustomerCreationBinding

class CustomerCreation : Fragment() {
    private var _binding: FragmentCustomerCreationBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //val fab = requireActivity().findViewById<FloatingActionButton>(com.moronlu18.invoice.R.id.fab)
        //fab.visibility = View.GONE

        // Inflate the layout for this fragment
        _binding = FragmentCustomerCreationBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}