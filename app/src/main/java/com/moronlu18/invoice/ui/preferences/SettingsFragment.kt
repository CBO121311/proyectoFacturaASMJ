package com.moronlu18.invoice.ui.preferences


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.moronlu18.invoice.Locator
import com.moronlu18.invoice.R
import com.moronlu18.invoice.ui.MainActivity

class SettingsFragment : PreferenceFragmentCompat() {

    //Para que podamos navegar de nuevo en setting estan en él.
    private var isInSettingFragment = false


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {


        setPreferencesFromResource(R.xml.settings_preferences, rootKey)
        setUpFab()

        //Obtener el DataStore que se quiere que utilicen los componentes visuales de las Preferencias.


        //Cuando se modifica el gestor de las preferencias se utiliza
        // en todos los PreferenceFragment de la jerarquia de vista
        //Ya no se utiliza el fichero shared_preferences

        preferenceManager.preferenceDataStore = Locator.settingsPreferencesRepository


        val accountPreferences =
            preferenceManager.findPreference<Preference>(getString(R.string.key_account))
        accountPreferences?.setOnPreferenceClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_acccountFragment)
            true
        }

        initPreferencesSortCustomer()
        initPreferencesInvoice()
        initPreferencesSortTask()
        initPreferencesBigText()
    }


    /**
     * Inicializa la preferencia de orden de la lista de clientes
     */
    private fun initPreferencesSortCustomer() {
        val sort = preferenceManager.findPreference<ListPreference>("key_sort_customer")

        sort?.value = Locator.settingsPreferencesRepository.getSortCustomer()
        updateSortSummary(sort)

        sort?.setOnPreferenceChangeListener { _, newValue ->

            Locator.settingsPreferencesRepository.saveSortCustomer(newValue as String)
            Handler(Looper.getMainLooper()).post {
                updateSortSummary(sort)
            }
            true
        }
    }

    private fun initPreferencesInvoice() {
        val sort = preferenceManager.findPreference<ListPreference>("key_sort_invoice")

        sort?.value = Locator.settingsPreferencesRepository.getSortInvoice()
        updateSortSummary(sort)

        sort?.setOnPreferenceChangeListener { _, newValue ->

            Locator.settingsPreferencesRepository.saveSortInvoice(newValue as String)
            Handler(Looper.getMainLooper()).post {
                updateSortSummary(sort)
            }
            true
        }
    }

    private fun initPreferencesSortTask() {
        val sort = preferenceManager.findPreference<ListPreference>("key_sort_task")

        sort?.value = Locator.settingsPreferencesRepository.getSortTask()
        updateSortSummary(sort)

        sort?.setOnPreferenceChangeListener { _, newValue ->

            Locator.settingsPreferencesRepository.saveSortTask(newValue as String)
            Handler(Looper.getMainLooper()).post {
                updateSortSummary(sort)
            }
            true
        }
    }

    /**
     * Actualiza el summary con la preferencia del orden actual
     */
    private fun updateSortSummary(sort: ListPreference?) {
        sort?.let {
            val index = it.findIndexOfValue(it.value)
            it.summary = if (index != -1) it.entries[index] else it.value
        }
    }

    /**
     * Inicializa la preferencia del tamaño del texto
     */
    private fun initPreferencesBigText() {
        val sizeText = preferenceManager.findPreference<SwitchPreference>("key_setting_text")

        sizeText?.isChecked =
            Locator.settingsPreferencesRepository.getBoolean("key_setting_text", false)

        sizeText?.setOnPreferenceChangeListener { _, newValue ->
            Locator.settingsPreferencesRepository.putBoolean(
                "key_setting_text",
                newValue as Boolean
            )
            true
        }
    }

    private fun setUpFab() {
        (requireActivity() as? MainActivity)?.fab?.apply {
            visibility = View.GONE
        }

        (requireActivity() as? MainActivity)?.toolbar?.menu?.findItem(R.id.action_settings)?.isVisible =
            false
    }


    override fun onResume() {
        super.onResume()
        isInSettingFragment = true

    }

    override fun onPause() {
        super.onPause()
        isInSettingFragment = false
    }

    override fun onDestroy() {
        super.onDestroy()
        (requireActivity() as? MainActivity)?.toolbar?.menu?.findItem(R.id.action_settings)?.isVisible =
            true
    }
}