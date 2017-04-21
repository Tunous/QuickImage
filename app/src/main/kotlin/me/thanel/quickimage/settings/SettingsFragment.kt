package me.thanel.quickimage.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.CheckBoxPreference
import android.preference.PreferenceFragment
import me.thanel.quickimage.R

class SettingsFragment : PreferenceFragment(), SharedPreferences.OnSharedPreferenceChangeListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addPreferencesFromResource(R.xml.settings)
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == KEY_PREF_SUCCESS_NOTIFICATION) {
            if (!sharedPreferences.getBoolean(key, true)) {
                // Disabling success notification force enables automatic copy to clipboard.
                // Otherwise the user wouldn't be able to access the newly uploaded link without
                // opening application.
                sharedPreferences.edit().putBoolean(KEY_PREF_AUTOMATIC_COPY, true).apply()
                (findPreference(KEY_PREF_AUTOMATIC_COPY) as CheckBoxPreference).isChecked = true
            }
        }
    }

    companion object {
        const val KEY_PREF_SUCCESS_NOTIFICATION = "pref_showSuccessNotification"
        const val KEY_PREF_AUTOMATIC_COPY = "pref_copyToClipboard"
    }
}
