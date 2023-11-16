package com.moronlu18.invoice.ui


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.moronlu18.invoice.R
import com.moronlu18.invoice.databinding.FragmentSplashBinding

private const val WAIT_TIME:Long = 1000
class SplashFragment : Fragment() {

    private var _binding : FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSplashBinding.inflate(inflater,container,false)
        return  binding.root
    }

    override fun onStart() {
        super.onStart()
        val r= Runnable { findNavController().navigate(R.id.action_splashFragment_to_mainFragment) }

        Handler(Looper.getMainLooper()).postDelayed(r, WAIT_TIME)
    }

}