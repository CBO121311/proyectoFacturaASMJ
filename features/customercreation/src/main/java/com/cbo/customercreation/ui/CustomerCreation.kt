package com.cbo.customercreation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.moronlu18.customercreation.R

class CustomerCreation : Fragment() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val fab = requireActivity().findViewById<FloatingActionButton>(com.moronlu18.invoice.R.id.fab)
        fab.visibility = View.GONE
        return inflater.inflate(R.layout.fragment_customer_creation, container, false)

    }
}