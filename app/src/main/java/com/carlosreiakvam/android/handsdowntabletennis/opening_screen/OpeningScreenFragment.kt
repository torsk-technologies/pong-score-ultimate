package com.carlosreiakvam.android.handsdowntabletennis.opening_screen

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.carlosreiakvam.android.handsdowntabletennis.databinding.OpeningScreenFragmentBinding

class OpeningScreenFragment : Fragment() {


    private lateinit var viewModel: OpeningScreenViewModel
    private lateinit var binding: OpeningScreenFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = OpeningScreenFragmentBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[OpeningScreenViewModel::class.java]
        return binding.root
    }


}