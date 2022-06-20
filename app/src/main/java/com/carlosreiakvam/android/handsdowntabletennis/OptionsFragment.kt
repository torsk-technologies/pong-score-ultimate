package com.carlosreiakvam.android.handsdowntabletennis

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat

class OptionsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.options, rootKey)
//        val mirrorPref: SwitchPreferenceCompat? = findPreference<SwitchPreferenceCompat>("mirrored")
    }

}