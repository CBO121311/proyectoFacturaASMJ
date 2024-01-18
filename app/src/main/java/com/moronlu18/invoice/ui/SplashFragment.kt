package com.moronlu18.invoice.ui



import android.content.res.Configuration
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import com.moronlu18.invoice.Locator
import com.moronlu18.invoice.R
import com.moronlu18.invoice.databinding.FragmentSplashBinding
import java.util.Locale

private const val WAIT_TIME: Long = 2000

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        _binding = FragmentSplashBinding.inflate(inflater, container, false)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val language =
            Locator.settingsPreferencesRepository.getSettingValue("key_language", "es")

        if (language != null) {
            updateLanguages(language)
        }

        val isNightMode = Locator.settingsPreferencesRepository.getBoolean("key_night_mode", false)

        if (isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        val animation = binding.ivAnimation.drawable as AnimationDrawable
        animation.start()
    }

    /**
     * Actualiza el lenguaje de la aplicación
     */
    private fun updateLanguages(language: String) {
        val newLocale = Locale(language)
        Locale.setDefault(newLocale)

        val configuration = Configuration()
        configuration.setLocale(newLocale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }




    override fun onStart() {
        super.onStart()
        val r = Runnable {
            //Return true if the fragment is currently added to its activity.
            if (isAdded) {
                findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
            }
        }
        Handler(Looper.getMainLooper()).postDelayed(r, WAIT_TIME)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}