package com.moronlu18.invoice.ui.preferences



import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.moronlu18.invoice.Locator
import com.moronlu18.invoice.R
import com.moronlu18.invoice.ui.MainActivity
import java.util.Locale


class SettingsFragment : PreferenceFragmentCompat() {

    //Para que podamos navegar de nuevo en setting estan en él.
    private var isInSettingFragment = false

    override fun onStart() {
        super.onStart()
        setUpFab()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {


        setPreferencesFromResource(R.xml.settings_preferences, rootKey)
        //setUpFab()

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
        initPreferencesLanguage()
        initPreferencesNight()
    }

    private fun initPreferencesNight() {
        val nightMode = preferenceManager.findPreference<SwitchPreference>("key_night_mode")

        nightMode?.isChecked =
            Locator.settingsPreferencesRepository.getBoolean("key_night_mode", false)

        nightMode?.setOnPreferenceChangeListener { _, newValue ->
            Locator.settingsPreferencesRepository.putBoolean(
                "key_night_mode",
                newValue as Boolean
            )
            if (newValue) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }

            true
        }
    }

    private fun initPreferencesLanguage() {
        val language = preferenceManager.findPreference<ListPreference>("key_language")

        language?.value =
            Locator.settingsPreferencesRepository.getSettingValue("key_language", "es")
        updateSummary(language)

        language?.setOnPreferenceChangeListener { _, newValue ->

            Locator.settingsPreferencesRepository.putSettingValue(
                "key_language",
                newValue as String
            )

            Handler(Looper.getMainLooper()).post {
                updateSummary(language)
            }
            updateLanguages(newValue)

            true
        }
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
        activity?.recreate()
    }


    /**
     * Inicializa la preferencia de orden de la lista de clientes
     */
    private fun initPreferencesSortCustomer() {
        val sort = preferenceManager.findPreference<ListPreference>("key_sort_customer")

        sort?.value = Locator.settingsPreferencesRepository.getSettingValue("customersort", "id")
        updateSummary(sort)

        sort?.setOnPreferenceChangeListener { _, newValue ->

            Locator.settingsPreferencesRepository.putSettingValue(
                "customersort",
                newValue as String
            )

            Handler(Looper.getMainLooper()).post {
                updateSummary(sort)
            }
            true
        }
    }

    private fun initPreferencesInvoice() {
        val sort = preferenceManager.findPreference<ListPreference>("key_sort_invoice")

        sort?.value = Locator.settingsPreferencesRepository.getSortInvoice()
        updateSummary(sort)

        sort?.setOnPreferenceChangeListener { _, newValue ->

            Locator.settingsPreferencesRepository.saveSortInvoice(newValue as String)
            Handler(Looper.getMainLooper()).post {
                updateSummary(sort)
            }
            true
        }
    }

    private fun initPreferencesSortTask() {
        val sort = preferenceManager.findPreference<ListPreference>("key_sort_task")

        sort?.value = Locator.settingsPreferencesRepository.getSettingValue("tasksort", "id")
        updateSummary(sort)

        sort?.setOnPreferenceChangeListener { _, newValue ->

            Locator.settingsPreferencesRepository.putSettingValue(
                "tasksort",
                newValue as String
            )
            Handler(Looper.getMainLooper()).post {
                updateSummary(sort)
            }
            true
        }
    }

    /**
     * Actualiza el summary con la preferencia seleccionada
     */
    private fun updateSummary(listOption: ListPreference?) {

        var index = listOption?.findIndexOfValue(listOption.value)

        if (index == -1){
            index = 0
        }

        val currEntry = listOption?.entries?.get(index!!)
        listOption?.summary = currEntry
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