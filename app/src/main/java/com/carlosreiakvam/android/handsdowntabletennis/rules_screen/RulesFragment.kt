package com.carlosreiakvam.android.handsdowntabletennis.rules_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.carlosreiakvam.android.handsdowntabletennis.R
import com.carlosreiakvam.android.handsdowntabletennis.databinding.RulesFragmentBinding

class RulesFragment : Fragment() {

    lateinit var binding: RulesFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = RulesFragmentBinding.inflate(layoutInflater, container, false)
        setupFirstServer()
        setupBestOfSlider()

        return binding.root
    }

    fun setupFirstServer() {
        val tableTop = binding.tableTop
        val tableBottom = binding.tableBottom
        val btnRandom = binding.btnRandom

        tableTop.setOnClickListener() {
            tableTop.text = "X"
            tableBottom.text = ""
        }
        tableBottom.setOnClickListener() {
            tableTop.text = ""
            tableBottom.text = "X"
        }
        btnRandom.setOnClickListener() {
            tableTop.text = "?"
            tableBottom.text = "?"
        }
    }

    fun setupBestOfSlider() {
        val bestOfLabel = binding.tvBestOfLabel
        bestOfLabel.text = getString(R.string.best_of, 21)

        val firsServerLabel = binding.tvFirstServerLabel
        firsServerLabel.text = getString(R.string.first_server, "Random")

        val sliderBestOf = binding.sliderBestOf
        sliderBestOf.value = 21f
        sliderBestOf.thumbRadius = 40


        sliderBestOf.addOnChangeListener { _, value, _ ->
            val roundedValue = value.toInt()
            bestOfLabel.text = getString(R.string.best_of, roundedValue)
        }
    }
}