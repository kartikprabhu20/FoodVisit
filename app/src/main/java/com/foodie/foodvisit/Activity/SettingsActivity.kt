package com.foodie.foodvisit.Activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.foodie.foodvisit.AppPreferenceManager
import com.foodie.foodvisit.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, PrefsFragment())
                .commit()
        }
    }

    @AndroidEntryPoint
    class PrefsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {

        @Inject
        lateinit var appPreferenceManager: AppPreferenceManager

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            addPreferencesFromResource(R.xml.settings)
            bindPreferenceSummaryToValue(
                findPreference(getString(R.string.pref_place_key))!!
            )
        }

        private fun bindPreferenceSummaryToValue(preference: Preference) {
            preference.onPreferenceChangeListener = this
            val currentValue = PreferenceManager
                .getDefaultSharedPreferences(preference.context)
                .getString(preference.key, "") ?: ""
            onPreferenceChange(preference, currentValue)
        }

        override fun onPreferenceChange(preference: Preference, value: Any): Boolean {
            val stringValue = value.toString()
            if (!appPreferenceManager.getLocation().equals(stringValue, ignoreCase = true)) {
                val listPreference = preference as? ListPreference
                if (listPreference != null) {
                    val prefIndex = listPreference.findIndexOfValue(stringValue)
                    if (prefIndex >= 0) {
                        preference.summary = listPreference.entries[prefIndex]
                    }
                }
                appPreferenceManager.setLocation(stringValue)
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
            }
            return true
        }
    }
}
