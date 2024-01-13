package com.moronlu18.invoice.ui.preferences

import android.os.Bundle
import android.text.InputType
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import com.moronlu18.invoice.Locator
import com.moronlu18.invoice.R

class AccountFragment : PreferenceFragmentCompat() {

    //Este es el único caso que no creamos un viewmodel
    // Ya que contacta sharedpreferences directamente
    //Ya que esto escribe directamente en el xml del datastore


    //escomo el create y onviewcreate
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.account_preferences, rootKey)
        //Obtener el DataStore que se quiere que utilicen los componentes visuales de las preferencias
        initPreferencesEmail()
        initPreferencesPassword()
    }


    private fun initPreferencesEmail() {
        val email =
            preferenceManager.findPreference<EditTextPreference>(getString(R.string.key_email))
        //Vamos a inicializar el texto de la preferencia
        email?.setOnBindEditTextListener {
            it.setText(Locator.userPreferencesRepository.getEmail())
            it.isEnabled = false
        }

    }


    private fun initPreferencesPassword() {
        val password =
            preferenceManager.findPreference<EditTextPreference>(getString(R.string.key_password))
        password?.setOnBindEditTextListener {
            it.setText(Locator.userPreferencesRepository.getPassword())
            it.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            it.selectAll()
        }

        //Si el usuario cambia el password se actualiza.
        password?.setOnPreferenceChangeListener { _, newpassword ->
            Locator.userPreferencesRepository.savePassword(newpassword as String)
            //devuelve de tipo Any, hacemos conversión
            true
        }
    }
}