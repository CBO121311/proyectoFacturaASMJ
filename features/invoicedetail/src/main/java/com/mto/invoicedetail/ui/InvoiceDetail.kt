package com.mto.invoicedetail.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.moronlu18.invoicedetail.databinding.FragmentInvoiceDetailBinding

class InvoiceDetail : Fragment() {


    private var _binding: FragmentInvoiceDetailBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentInvoiceDetailBinding.inflate(inflater, container, false)

        return binding.root;

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}