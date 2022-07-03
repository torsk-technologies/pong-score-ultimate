package com.carlosreiakvam.android.handsdowntabletennis.opening_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.carlosreiakvam.android.handsdowntabletennis.databinding.OpeningScreenFragmentBinding

class OpeningScreenFragment : Fragment() {


    private val args: OpeningScreenFragmentArgs by navArgs()
    private lateinit var binding: OpeningScreenFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = OpeningScreenFragmentBinding.inflate(inflater)
        binding.btnContinue.isVisible = !args.gameFinished
        setupClickListeners()

        // Force portrait orientation
//        activity?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        return binding.root
    }

    private fun setupClickListeners() {
        // New game button
        binding.btnNewGame.setOnClickListener {
            this.findNavController()
                .navigate(OpeningScreenFragmentDirections.actionOpeningScreenFragmentToRulesFragment())
        }
        // Continue button
        binding.btnContinue.setOnClickListener {
            this.findNavController()
                .navigate(OpeningScreenFragmentDirections.actionOpeningScreenFragmentToPlayFragment(
                    bestOf = -1,
                    firstServer = -1))
        }
        // Info button
        binding.btnInfo.setOnClickListener {
            this.findNavController()
                .navigate(OpeningScreenFragmentDirections.actionOpeningScreenFragmentToAboutFragment())
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        requireActivity().window.statusBarColor =
//            ContextCompat.getColor(requireContext(), R.color.azure)
    }
}