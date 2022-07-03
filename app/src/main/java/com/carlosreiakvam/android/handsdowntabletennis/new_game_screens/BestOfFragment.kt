package com.carlosreiakvam.android.handsdowntabletennis.new_game_screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.carlosreiakvam.android.handsdowntabletennis.R
import com.carlosreiakvam.android.handsdowntabletennis.databinding.BestOfFragmentBinding

class BestOfFragment : Fragment() {

    private lateinit var binding: BestOfFragmentBinding
    private var bestOf: Int = 21

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = BestOfFragmentBinding.inflate(layoutInflater, container, false)
        setupBestOfSlider()
        setupPlayButton()

        return binding.root
    }


    private fun setupBestOfSlider() {
        val bestOfLabel = binding.tvBestOfLabel
        bestOfLabel.text = getString(R.string.best_of, 21)


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
        val btnPlay = binding.btnNext
        btnPlay.setOnClickListener {
            this.findNavController()
                .navigate(BestOfFragmentDirections.actionBestOfFragmentToFirstServerFragment(bestOf = bestOf))
        }
    }
}