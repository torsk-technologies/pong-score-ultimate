package com.carlosreiakvam.android.handsdowntabletennis.opening_screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.carlosreiakvam.android.handsdowntabletennis.databinding.OpeningScreenFragmentBinding

class OpeningScreenFragment : Fragment() {


    private lateinit var binding: OpeningScreenFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = OpeningScreenFragmentBinding.inflate(inflater)
        binding.btnLetsplay.setOnClickListener {
            this.findNavController()
                .navigate(OpeningScreenFragmentDirections.actionOpeningScreenFragmentToPlayFragment())
        }
        return binding.root
    }


}