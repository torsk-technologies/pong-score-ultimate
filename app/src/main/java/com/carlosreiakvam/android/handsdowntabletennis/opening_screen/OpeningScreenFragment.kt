package com.carlosreiakvam.android.handsdowntabletennis.opening_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.carlosreiakvam.android.handsdowntabletennis.R
import com.carlosreiakvam.android.handsdowntabletennis.databinding.OpeningScreenFragmentBinding

class OpeningScreenFragment : Fragment() {


    private lateinit var binding: OpeningScreenFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = OpeningScreenFragmentBinding.inflate(inflater)

        // Force portrait orientation
//        activity?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

//        binding.btnOptions.setOnClickListener() {
//            this.findNavController()
//                .navigate(OpeningScreenFragmentDirections.actionOpeningScreenFragmentToOptionsFragment())
//        }

        binding.btnLetsplay.setOnClickListener {
            this.findNavController()
                .navigate(OpeningScreenFragmentDirections.actionOpeningScreenFragmentToPlayFragment())
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.azure)
    }


}