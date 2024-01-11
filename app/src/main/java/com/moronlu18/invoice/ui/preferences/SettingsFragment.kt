package com.moronlu18.invoice.ui.preferences

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.moronlu18.invoice.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_preferences, rootKey)

        val accountPreferences =  preferenceManager.findPreference<Preference>(getString(R.string.key_account))
        accountPreferences?.setOnPreferenceClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_acccountFragment)
            true
        }
    }
}