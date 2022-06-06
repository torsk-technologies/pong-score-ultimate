package com.carlosreiakvam.android.handsdowntabletennis.play_screen

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.carlosreiakvam.android.handsdowntabletennis.databinding.PlayFragmentBinding

class PlayFragment : Fragment() {


    private lateinit var viewModel: PlayViewModel
    private lateinit var binding: PlayFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[PlayViewModel::class.java]
        binding = PlayFragmentBinding.inflate(inflater)
        return binding.root
    }


}