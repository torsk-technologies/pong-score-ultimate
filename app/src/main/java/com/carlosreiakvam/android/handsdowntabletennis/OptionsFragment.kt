package com.carlosreiakvam.android.handsdowntabletennis

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SeekBarPreference

class OptionsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.options, rootKey)

        val seekBarPreference: SeekBarPreference =
            findPreference("best_of") ?: SeekBarPreference(requireContext())
        seekBarPreference.seekBarIncrement = 3
        seekBarPreference.min = 3
        seekBarPreference.max = 21
        seekBarPreference.setDefaultValue(3)
    }

}