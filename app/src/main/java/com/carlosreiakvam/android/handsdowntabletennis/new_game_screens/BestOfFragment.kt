package com.carlosreiakvam.android.handsdowntabletennis.new_game_screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.carlosreiakvam.android.handsdowntabletennis.databinding.BestOfFragmentBinding
import com.carlosreiakvam.android.handsdowntabletennis.play_screen.InitialValues

class BestOfFragment : Fragment() {

    private lateinit var binding: BestOfFragmentBinding
    private var bestOf: Int = InitialValues.BESTOF.i

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
        binding.btnBack.setOnClickListener {
            activity?.onBackPressed()
        }
    }


    private fun setupBestOfSlider() {
        val bestOfNumber = binding.tvBestOfNumber
        bestOfNumber.text = InitialValues.BESTOF.i.toString()


        val sliderBestOf = binding.sliderBestOf
        sliderBestOf.value = InitialValues.BESTOF.i.toFloat()


        sliderBestOf.addOnChangeListener { _, value, _ ->
            val roundedValue = value.toInt()
            bestOfNumber.text = roundedValue.toString()
            bestOf = roundedValue
        }
    }

    private fun setupNextButtons() {

        binding.btnNext.setOnClickListener {
            this.findNavController()
                .navigate(BestOfFragmentDirections.actionBestOfFragmentToFirstServerFragment(bestOf))
        }
    }
}