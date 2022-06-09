package com.carlosreiakvam.android.handsdowntabletennis.play_screen

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
    private var playerOneHasTopPos = true


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.play_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel

        binding.player1Container.setOnClickListener {
            viewModel.p1IncreaseGameScore()
        }
        binding.player2Container.setOnClickListener {
            viewModel.p2IncreaseGameScore()
        }

    }

//    override fun onConfigurationChanged(newConfig: Configuration) {
//        Log.d("TAG", "onConfigurationChanged")
//        super.onConfigurationChanged(newConfig)
//        Log.d("TAG", requireActivity().requestedOrientation.toString())
//        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            requireActivity().setContentView(R.layout.play_fragment_upside_down)
//        }
//    }


}