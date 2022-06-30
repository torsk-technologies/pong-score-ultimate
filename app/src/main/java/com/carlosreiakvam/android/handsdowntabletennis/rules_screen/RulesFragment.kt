package com.carlosreiakvam.android.handsdowntabletennis.rules_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.carlosreiakvam.android.handsdowntabletennis.R
import com.carlosreiakvam.android.handsdowntabletennis.databinding.RulesFragmentBinding
import kotlin.random.Random

class RulesFragment : Fragment() {

    private lateinit var binding: RulesFragmentBinding
    private var firstServer: Int = 1
    private var bestOf: Int = 21

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = RulesFragmentBinding.inflate(layoutInflater, container, false)
        setupFirstServer()
        setupBestOfSlider()
        setupPlayButton()

        return binding.root
    }


    private fun setupFirstServer() {
        val tableTop = binding.tableTop
        val tableBottom = binding.tableBottom
        val btnRandom = binding.btnRandom

        tableTop.text = "X"

        tableTop.setOnClickListener {
            firstServer = 1
            tableTop.text = "X"
            tableBottom.text = ""
        }
        tableBottom.setOnClickListener {
            firstServer = 2
            tableTop.text = ""
            tableBottom.text = "X"
        }
        btnRandom.setOnClickListener {
            when (Random.nextInt(1, 2)) {
                1 -> firstServer = 1
                2 -> firstServer = 2
            }
            tableTop.text = "?"
            tableBottom.text = "?"
        }
    }

    private fun setupBestOfSlider() {
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
            bestOf = roundedValue
        }
    }

    private fun setupPlayButton() {
        val btnPlay = binding.btnLetsPlay
        btnPlay.setOnClickListener {
            this.findNavController()
                .navigate(RulesFragmentDirections.actionRulesFragmentToPlayFragment(
                    bestOf = bestOf,
                    firstServer = firstServer))

        }
    }
}