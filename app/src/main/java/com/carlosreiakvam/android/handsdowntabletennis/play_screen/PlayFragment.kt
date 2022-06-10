package com.carlosreiakvam.android.handsdowntabletennis.play_screen

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.carlosreiakvam.android.handsdowntabletennis.R
import com.carlosreiakvam.android.handsdowntabletennis.databinding.PlayFragmentBinding

class PlayFragment : Fragment() {

    private lateinit var binding: PlayFragmentBinding
    private val viewModel: PlayViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.play_fragment, container, false)

//        // Force landscape orientation
//        activity?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel

        binding.player1Container.setOnClickListener {
            viewModel.increaseGameScore(1)
        }

        binding.player2Container.setOnClickListener {
            viewModel.increaseGameScore(2)
        }
    }


}
