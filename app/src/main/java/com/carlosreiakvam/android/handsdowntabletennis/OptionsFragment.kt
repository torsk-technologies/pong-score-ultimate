package com.carlosreiakvam.android.handsdowntabletennis

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat

class OptionsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.options, rootKey)
        val mirrorPref: SwitchPreferenceCompat? = findPreference<SwitchPreferenceCompat>("mirrored")
    }

}