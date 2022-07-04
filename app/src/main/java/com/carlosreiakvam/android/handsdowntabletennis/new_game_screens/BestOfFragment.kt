package com.carlosreiakvam.android.handsdowntabletennis.new_game_screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
        setupNextButtons()
        setupBackButton()

        return binding.root
    }

    private fun setupBackButton() {
        binding.btnBack.setOnClickListener{
            activity?.onBackPressed()
        }
    }


    private fun setupBestOfSlider() {
        val bestOfNumber = binding.tvBestOfNumber
        bestOfNumber.text = "9"


        val sliderBestOf = binding.sliderBestOf
        sliderBestOf.value = 9f


        sliderBestOf.addOnChangeListener { _, value, _ ->
            val roundedValue = value.toInt()
            bestOfNumber.text = roundedValue.toString()
            bestOf = roundedValue
        }
    }

    private fun setupNextButtons() {

        binding.upperTable.setOnClickListener {
            this.findNavController()
                .navigate(BestOfFragmentDirections.actionBestOfFragmentToFirstServerFragment(bestOf))
        }
        binding.lowerTable.setOnClickListener {
            this.findNavController()
                .navigate(BestOfFragmentDirections.actionBestOfFragmentToFirstServerFragment(bestOf))
        }
//        val btnPlay = binding.btnNext
//        btnPlay.setOnClickListener {
//            this.findNavController()
//                .navigate(BestOfFragmentDirections.actionBestOfFragmentToFirstServerFragment(bestOf = bestOf))
//        }
    }
}