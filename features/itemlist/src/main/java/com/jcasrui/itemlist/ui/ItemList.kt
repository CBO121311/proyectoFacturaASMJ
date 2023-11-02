package com.jcasrui.itemlist.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.moronlu18.itemlist.R

class ItemList : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val fab = requireActivity().findViewById<FloatingActionButton>(com.moronlu18.invoice.R.id.fab)
        fab.visibility = View.GONE

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_item_list, container, false)
    }
}