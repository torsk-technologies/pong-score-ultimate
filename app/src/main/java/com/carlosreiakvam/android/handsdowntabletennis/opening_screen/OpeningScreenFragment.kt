package com.carlosreiakvam.android.handsdowntabletennis.opening_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.carlosreiakvam.android.handsdowntabletennis.databinding.OpeningScreenFragmentBinding

class OpeningScreenFragment : Fragment() {


//    private val args: OpeningScreenFragmentArgs by navArgs()
    private lateinit var binding: OpeningScreenFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = OpeningScreenFragmentBinding.inflate(inflater)
        setupClickListeners()

        // Force portrait orientation
//        activity?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        return binding.root
    }

    private fun setupClickListeners() {
        binding.btnLetsGo.setOnClickListener {
            this.findNavController()
                .navigate(OpeningScreenFragmentDirections.actionOpeningScreenFragmentToPlayFragment())
        }

        binding.btnInfo.setOnClickListener {
            this.findNavController()
                .navigate(OpeningScreenFragmentDirections.actionOpeningScreenFragmentToAboutFragment())
        }
    }


}