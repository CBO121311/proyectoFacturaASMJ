package com.moronlu18.invoice.ui


import android.annotation.SuppressLint
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

private const val WAIT_TIME: Long = 2000

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSplashBinding.inflate(inflater, container, false)

        animationLogo()

        return binding.root
    }


    @SuppressLint("ResourceType")
    private fun animationLogo() {


         binding.imgLogo.animate().apply {
             duration = 1500
             scaleXBy(0.5f)
             scaleYBy(0.5f)
             rotationYBy(360f)
             translationYBy(200f)
         }.start()
    }

    override fun onStart() {
        super.onStart()
        val r =
            Runnable { findNavController().navigate(R.id.action_splashFragment_to_mainFragment) }
        // findNavController().navigate(R.id.action_splashFragment_to_mainFragment).bundle(e)
        Handler(Looper.getMainLooper()).postDelayed(r, WAIT_TIME)
    }

}