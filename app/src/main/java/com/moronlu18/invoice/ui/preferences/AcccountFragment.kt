package com.moronlu18.invoice.ui.preferences

import android.os.Bundle
import android.text.InputType
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import com.moronlu18.invoice.R

class AcccountFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.account_preferences, rootKey)

        initPreferencesEmail()
        initPreferencesPassword()
    }


    private fun initPreferencesEmail() {
        val email = preferenceManager.findPreference<EditTextPreference>(getString(R.string.key_email))
        //Vamos a inicializar el texto de la preferencia
        email?.setOnBindEditTextListener{
            it.setText("inforamcion@iesportada.org")
            it.isEnabled = false
        }

    }


    private fun initPreferencesPassword() {
        val password = preferenceManager.findPreference<EditTextPreference>(getString(R.string.key_password))
        password?.setOnBindEditTextListener {
            it.setText("12345678")
            it.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            it.selectAll()

        }
    }

}