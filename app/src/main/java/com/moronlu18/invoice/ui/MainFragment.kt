package com.moronlu18.invoice

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btSignin.setOnClickListener {
            //findNavController().navigate()
            //findNavController().navigate(R.id.action_mainFragment_to_featureAccountSignIn)
            findNavController().navigate(R.id.action_mainFragment_to_featureAccountSignIn)
        }
        binding.btSignUp.setOnClickListener {
            //findNavController().navigate()
            findNavController().navigate(R.id.action_mainFragment_to_featureAccountSignUp)
        }

        //Customer
        binding.btCustomerCreation.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_CustomerCreationFragment)
        }

        binding.btCustomerDetails.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_CustomerDetailFragment)
        }

        binding.btCustomerList.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_CustomerListFragment)
        }

        //Invoice
        binding.btInvoiceCreation.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_InvoiceCreationFragment)
        }

        binding.btInvoiceDetails.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_InvoiceDetailFragment)
        }

        binding.btInvoiceList.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_InvoiceListFragment)
        }

        //Item
        binding.btItemCreation.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_ItemCreationFragment)
        }

        binding.btItemDetails.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_ItemDetailFragment)
        }

        binding.btItemList.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_ItemListFragment)
        }


        //Task
        binding.btTaskCreation.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_TaskCreationFragment)
        }

        binding.btTaskDetails.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_TaskDetailFragment)
        }

        binding.btTaskList.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_TaskListFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}